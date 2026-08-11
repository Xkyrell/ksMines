package me.xkyrell.ksmines.util

import me.xkyrell.temporal.TemporalUnit
import me.xkyrell.temporal.format.impl.TextualTemporalFormatter
import me.xkyrell.temporal.format.impl.TextualTemporalParser
import me.xkyrell.temporal.format.registry.TemporalEntry
import me.xkyrell.temporal.format.registry.TemporalRegistry
import me.xkyrell.temporal.format.style.TextualTemporalStyle
import java.util.function.Supplier

class TemporalStyles {

    private class InternalTemporalRegistry : TemporalRegistry<String, TemporalEntry> {

        override fun loadStyles(): Map<String, TemporalEntry> =
            mapOf(
                "d" to entry(TemporalUnit.DAYS, "d"),
                "h" to entry(TemporalUnit.HOURS, "h"),
                "m" to entry(TemporalUnit.MINUTES, "m"),
                "s" to entry(TemporalUnit.SECONDS, "s")
            )

        private fun entry(
            unit: TemporalUnit,
            name: String
        ): TemporalEntry =
            TemporalEntry(
                unit,
                arrayOf(name)
            )
    }

    companion object {
        private val DEFAULT_REGISTRY =
            InternalTemporalRegistry()

        val DEFAULT_STYLE: TextualTemporalStyle = createTextual(DEFAULT_REGISTRY)

        fun createTextual(
            registry: TemporalRegistry<String, TemporalEntry>
        ): TextualTemporalStyle =
            TextualTemporalStyle.builder()
                .formatter(
                    Supplier { TextualTemporalFormatter() }
                )
                .parser(
                    Supplier { TextualTemporalParser() }
                )
                .unit(registry)
                .pluralize { 0 }
                .includeSmallestUnit()
                .build()
    }
}