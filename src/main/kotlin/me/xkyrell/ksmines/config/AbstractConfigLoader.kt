package me.xkyrell.ksmines.config

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.KSerializer
import java.nio.file.Files
import java.nio.file.Path

abstract class AbstractConfigLoader(
    protected val dataFolder: Path
) {

    protected abstract val yaml: Yaml

    protected fun <T> load(
        path: Path,
        serializer: KSerializer<T>
    ): T {
        try {
            return yaml.decodeFromString(
                serializer,
                Files.readString(path)
            )
        }
        catch (ex: Exception) {
            throw IllegalStateException(
                "Failed to load ${path.fileName}", ex
            )
        }
    }

    protected fun <T> loadOrCreate(
        path: Path,
        serializer: KSerializer<T>,
        fallback: () -> T
    ): T {
        if (!Files.exists(path)) {
            val value = fallback()

            save(
                path,
                serializer,
                value
            )

            return value
        }

        try {
            val content = Files.readString(path)

            if (content.isBlank()) {
                return fallback()
            }
            else {
                return yaml.decodeFromString(
                    serializer,
                    content
                )
            }
        }
        catch (ex: Exception) {
            throw IllegalStateException(
                "Failed to load ${path.fileName}", ex
            )
        }
    }

    protected fun <T> save(
        path: Path,
        serializer: KSerializer<T>,
        value: T
    ) {
        try {
            Files.createDirectories(
                path.parent
            )

            Files.writeString(
                path,
                yaml.encodeToString(
                    serializer,
                    value
                )
            )
        } catch (ex: Exception) {
            throw IllegalStateException(
                "Failed to save ${path.fileName}", ex
            )
        }
    }

    abstract fun load()

    abstract fun reload()

    abstract fun save()

}