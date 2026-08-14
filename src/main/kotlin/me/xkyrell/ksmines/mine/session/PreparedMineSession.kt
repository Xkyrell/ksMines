package me.xkyrell.ksmines.mine.session

import me.xkyrell.ksmines.mine.Mine
import me.xkyrell.ksmines.mine.MineTemplate
import me.xkyrell.ksmines.mine.block.MineBlock
import me.xkyrell.ksmines.util.*
import net.kyori.adventure.key.Key
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

internal open class PreparedMineSession(
    private val key: NamespacedKey,
    private val owner: Player,
    private val targetIdentity: Key,
    private val template: MineTemplate,
    private val editorItem: ItemStack,
    private val editedStage: Int,
    private var region: Cuboid?,
    private var refillLocation: Location?,
) : MineSession {

    private val modifiedBlocks: MutableSet<MineBlock> = LinkedHashSet(
        template.rotationStages.getOrNull(editedStage)?.blocks
            ?: throw IndexOutOfBoundsException(
                "Invalid mine stage index: $editedStage"
            )
    )

    private var minPoint: Location? = region?.minPoint?.clone()
    private var maxPoint: Location? = region?.maxPoint?.clone()

    private val inventorySnapshot = PlayerInventorySnapshot.capture(owner)
    private var closed = false

    private val sessionIdentity: String get() {
        return "${targetIdentity.namespace()}-${targetIdentity.value()}"
    }

    init {
        owner.inventory.clear()
        editorItem.editPersistentDataContainer {
            it.set(key, PersistentDataType.STRING, sessionIdentity)
        }
        owner.inventory.setItem(0, editorItem.clone())
    }

    override fun setRegion(region: Cuboid) {
        check(!closed) {
            "session is already closed"
        }

        this.region = region
    }

    override fun modifyMinPoint(location: Location) {
        check(!closed) {
            "session is already closed"
        }

        minPoint = location.clone()
    }

    override fun modifyMaxPoint(location: Location) {
        check(!closed) {
            "session is already closed"
        }

        maxPoint = location.clone()
    }

    override fun setRefillLocation(location: Location?) {
        check(!closed) {
            "session is already closed"
        }

        refillLocation = location?.clone()
    }

    override fun addBlock(block: MineBlock) {
        check(!closed) {
            "session is already closed"
        }

        modifiedBlocks.add(block)
    }

    override fun removeBlock(block: MineBlock) {
        check(!closed) {
            "session is already closed"
        }

        modifiedBlocks.remove(block)
    }

    override fun removeBlockIf(predicate: (MineBlock) -> Boolean) {
        check(!closed) {
            "session is already closed"
        }

        modifiedBlocks.removeIf(predicate)
    }

    override fun modifyBlockIfPresent(modifier: (MineBlock) -> MineBlock?) {
        check(!closed) {
            "session is already closed"
        }

        val modified = LinkedHashSet<MineBlock>(modifiedBlocks.size)
        for (block in modifiedBlocks) {
            modified.add(modifier(block) ?: block)
        }

        modifiedBlocks.clear()
        modifiedBlocks.addAll(modified)
    }

    override fun getKey(): NamespacedKey = key

    override fun getOwner(): Player = owner

    override fun getEditedStage(): Int = editedStage

    override fun getTargetIdentity(): Key = targetIdentity

    override fun getModifiedBlocks(): Set<MineBlock> {
        return modifiedBlocks.toSet()
    }

    override fun getTarget(): Mine? = null

    override fun isClosed(): Boolean = closed

    override fun isSessionItem(item: ItemStack): Boolean {
        val value = item.persistentDataContainer.get(key, PersistentDataType.STRING)
        return value == sessionIdentity
    }

    override fun applyChanges(): MineSessionResult {
        check(!closed) {
            "session is already closed"
        }

        try {
            val modifiedTemplate = createModifiedTemplate()
            val noChanges = modifiedTemplate == template
            return MineSessionResult(
                targetIdentity,
                modifiedTemplate,
                noChanges,
                editedStage
            )
        }
        finally {
            close()
        }
    }

    private fun createModifiedTemplate(): MineTemplate {
        val stages = template.rotationStages.toMutableList()
        stages[editedStage] = stages[editedStage].copy(
            blocks = modifiedBlocks.toSet()
        )

        return template.copy(
            rotationStages = stages,
            region = createRegion(),
            refillLocation = refillLocation
        )
    }

    private fun createRegion(): Cuboid? {
        val minPoint = minPoint ?: return region
        val maxPoint = maxPoint ?: return region

        if (minPoint == region?.minPoint && maxPoint == region?.maxPoint) {
            return region
        }

        return Cuboid.of(minPoint, maxPoint)
    }

    override fun close() {
        check(!closed) {
            "session is already closed"
        }

        closed = true
        owner.inventory.clear()
        inventorySnapshot.restore(owner)
        modifiedBlocks.clear()
    }
}