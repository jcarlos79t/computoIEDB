# Reglas para kotlinx.serialization
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}

-keep class * implements kotlinx.serialization.KSerializer {
    <fields>;
    <methods>;
}

-keep class *$$serializer {
    <fields>;
    <methods>;
}

# Conservar los modelos de datos de tu aplicación
-keep class org.jct.iedbs1.models.** { *; }

# Reglas generales para Coroutines y Kotlin
-dontwarn kotlinx.coroutines.debug.**
-keepclassmembers class kotlinx.coroutines.internal.MainDispatcherFactory { 
    private static final kotlinx.coroutines.MainCoroutineDispatcher a;
}

# Otras reglas comunes para Compose
-keepclassmembers class ** { 
    @androidx.compose.runtime.Composable <methods>;
}
