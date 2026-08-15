package me.xkyrell.ksmines.mine.block

import me.xkyrell.ksmines.util.Cuboid
import me.xkyrell.ksmines.util.random.WeightSet

interface MineBlockProcessor {

    fun populate(region: Cuboid, blocks: WeightSet<MineBlock>)

    fun depopulate(region: Cuboid)

}