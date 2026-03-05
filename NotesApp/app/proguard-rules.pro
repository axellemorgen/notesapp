# ProGuard rules for Secure Notes app

-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Room database
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keepclassmembers class * extends androidx.room.RoomDatabase {
  public static ** getDatabase(...);
}

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}

# AndroidX
-keep class androidx.** { *; }
-dontwarn androidx.**

# Biometric
-keep class androidx.biometric.** { *; }

# DataStore
-keep class androidx.datastore.** { *; }
