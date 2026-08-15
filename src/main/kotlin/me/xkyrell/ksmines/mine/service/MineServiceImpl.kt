package me.xkyrell.ksmines.mine.service

import me.xkyrell.ksmines.mine.Mine
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.KeyPattern
import org.bukkit.Material
import javax.inject.Inject

class MineServiceImpl @Inject constructor() : MineService {

    private val mines = HashMap<Key, Mine>()

    override fun register(mine: Mine) {
        val key = mine.key()
        require(mines.putIfAbsent(key, mine) == null) {
            "Mine '$key' is already registered"
        }

        mine.setActive(true)
    }

    override fun unregister(key: Key, cleanUp: Boolean) {
        mines.remove(key)?.also {
            it.setActive(false)
            if (cleanUp) {
                val region = it.getTemplate().getRegionOrThrow()
                it.getProcessor().depopulate(region)
            }
        }
    }

    override fun unregisterAll(cleanUp: Boolean) {
        val iterator = mines.values.iterator()
        while (iterator.hasNext()) {
            val mine = iterator.next()
            mine.setActive(false)
            if (cleanUp) {
                val region = mine.getTemplate().getRegionOrThrow()
                mine.getProcessor().depopulate(region)
            }

            iterator.remove()
        }
    }

    override fun getResolver(): MineResolver {
        return MineResolverImpl(mines)
    }

    private inner class MineResolverImpl(
        private val mines: Map<Key, Mine>
    ) : MineResolver {

        override fun resolve(@KeyPattern key: String): Mine? {
            return mines.entries
                .firstOrNull { it.key.asString() == key }
                ?.value
        }

        override fun resolve(key: Key): Mine? {
            return mines[key]
        }

        override fun resolveOrThrow(
            @KeyPattern name: String
        ): Mine {
            return resolve(name)
                ?: error("Mine '$name' is not registered")
        }

        override fun resolveOrThrow(key: Key): Mine {
            return resolve(key)
                ?: error("Mine '$key' is not registered")
        }

        override fun getActiveMines(): Collection<Mine> {
            return mines.values.filter { it.isActive() }
        }

        override fun getMines(): Collection<Mine> {
            return mines.values
        }
    }
}