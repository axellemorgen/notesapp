# Secure Notes - Application Android

Une application de prise de notes sécurisée avec support des checklists, formatage de texte, images, audio et zone secrète protégée par biométrie.

## 🚀 Caractéristiques

✅ **Notes standard** - Créez et modifiez des notes avec titre et contenu  
✅ **Checklists** - Créez des listes de tâches avec sous-tâches  
✅ **Formatage** - Gras, italique, 3 tailles de texte  
✅ **Couleurs** - Personnalisez la couleur de fond de chaque note  
✅ **Médias** - Insérez des images et de l'audio  
✅ **Tags** - Catégorisez vos notes avec des tags  
✅ **Recherche** - Trouvez rapidement vos notes  
✅ **Épinglage** - Gardez les notes importantes en haut  
✅ **Archive** - Archivez les notes sans les supprimer  
✅ **Zone Secrète** 🔒 - Accès protégé par biométrie + PIN  
✅ **Thème clair/sombre** - Suivit les paramètres système

## 📋 Prérequis

- Android Studio (version 2022.1 ou plus récente)
- SDK Android 26+ (pour compilation)
- Android 8.0+ (pour exécution)

## 🛠️ Installation

### 1. Ouvrir le projet dans Android Studio

1. Lancez Android Studio
2. Allez à **File** → **Open**
3. Sélectionnez le dossier `NotesApp`
4. Attendez que Gradle finisse la synchronisation

### 2. Compiler et générer l'APK

**Pour l'emulateur :**
1. Allez à **Build** → **Build Bundles / APKs** → **Build APK(s)**
2. Attendez la fin de la compilation
3. Un message confirmera la création du fichier

**Le fichier APK se trouvera à :**
```
app/release/app-release.apk
```

### 3. Installer sur votre téléphone

#### Option A : Via USB (Recommandé)
1. Branchez votre téléphone en USB
2. Activez le mode développeur (Paramètres → À propos → Appuyez 7x sur "Numéro de version")
3. Dans Android Studio : **Run** → **Run 'app'**

#### Option B : Transférer l'APK
1. Transférez `app-release.apk` vers votre téléphone
2. Ouvrez le fichier avec le gestionnaire d'application
3. Appuyez sur **Installer**

## 📖 Guide d'utilisation

### Première utilisation
1. À la première ouverture, configurez un **code PIN** (4-8 chiffres)
2. Optionnellement, activez la **reconnaissance d'empreinte**

### Créer une note
1. Appuyez sur le bouton **+**
2. Entrez un titre et des tags
3. Modifiez le contenu, les couleurs, ajoutez des images
4. Appuyez sur ✓ pour enregistrer

### Zone Secrète 🔒
1. **Glissez votre doigt du haut vers le bas** pour accéder
2. Authentifiez-vous avec :
   - 👆 Votre empreinte digitale (le plus rapide)
   - ou 🔐 Votre code PIN
3. Déplacez les notes vers la zone secrète via le menu

### Recherche & Filtrage
- Utilisez la barre de recherche en haut
- Cliquez sur les tags pour filtrer
- Épinglez les notes importantes

## 🔒 Sécurité

- Les notes secrètes sont séparées du reste
- Authentification biométrique + PIN
- Les données sont chiffrées localement
- Aucun accès réseau

## 📱 Spécifications

- **Language** : Kotlin
- **UI** : Jetpack Compose + Material Design 3
- **Database** : Room
- **Min SDK** : 26 (Android 8.0)
- **Target SDK** : 34 (Android 14)

## 🐛 Dépannage

### L'APK ne se génère pas
```
Build → Clean Project
Build → Rebuild Project
```

### Erreur de synchronisation Gradle
```
File → Sync Now
```

### L'app ne s'installe pas
- Vérifiez que "Sources inconnues" est activé
- Désinstallez la version précédente
- Nettoyez le cache : Settings → Applications → Secure Notes → Forcer l'arrêt

## 📝 Notes

- Sauvegarde locale uniquement (pas de cloud par défaut)
- Les notes archivées peuvent être restaurées
- Suppression définitive impossible après archivage

## 🤝 Support

Pour toute question ou bug, consultez le code source - tout est commenté !

---

**Développé avec ❤️ en Kotlin**
