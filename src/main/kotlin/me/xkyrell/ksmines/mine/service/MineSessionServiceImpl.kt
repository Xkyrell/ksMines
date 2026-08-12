package me.xkyrell.ksmines.mine.service

import me.xkyrell.ksmines.mine.Mine
import me.xkyrell.ksmines.mine.session.MineSession
import org.bukkit.entity.Player
import java.util.UUID
import javax.inject.Inject

class MineSessionServiceImpl @Inject constructor() : MineSessionService {

    private val sessions = HashMap<UUID, MineSession>()

    override fun register(session: MineSession) {
        val uuid = session.getOwner().uniqueId;
        require(sessions.putIfAbsent(uuid, session) == null) {
            "Session for '${session.getTargetIdentity()}' is already registered"
        }
    }

    override fun unregister(owner: Player): Boolean {
        val session = sessions.remove(owner.uniqueId)
        if (session != null) {
            session.close()
            return true
        }
        return false
    }

    override fun terminate(owner: Player): MineSession? {
        return sessions.remove(owner.uniqueId)
    }

    override fun unregisterAll() {
        val iterator = sessions.values.iterator()
        while (iterator.hasNext()) {
            iterator.next().close()
            iterator.remove()
        }
    }

    override fun getResolver(): MineSessionResolver {
        return MineSessionResolverImpl(sessions)
    }

    private inner class MineSessionResolverImpl(
        private val sessions: Map<UUID, MineSession>
    ) : MineSessionResolver {

        override fun resolveSession(owner: Player): MineSession? {
            return sessions[owner.uniqueId]
        }

        override fun resolveSession(mine: Mine): MineSession? {
            return sessions.values.firstOrNull {
                it.getTarget() == mine
            }
        }

        override fun getSessions(): Collection<MineSession> {
            return sessions.values
        }
    }
}