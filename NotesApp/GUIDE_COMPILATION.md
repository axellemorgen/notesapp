# 📱 Guide de Compilation de Secure Notes

Ce guide vous explique comment compiler l'application et générer l'APK à installer sur votre téléphone.

## Étape 1️⃣ : Installer les outils

### Sur Windows/Mac/Linux

Vous avez besoin de :
1. **Android Studio** - L'IDE officiel (gratuit)
   - Téléchargez sur : https://developer.android.com/studio
   - Installez en acceptant tous les éléments par défaut

2. **JDK 11+** (généralement inclus avec Android Studio)

### Vérifier l'installation

Une fois Android Studio installé :
1. Ouvrez-le
2. Allez à **Tools** → **SDK Manager**
3. Vérifiez que vous avez **SDK 34** d'installé
4. Acceptez les licences : **Tools** → **SDK Manager** → **SDK Platforms** → Onglet "Licenses"

## Étape 2️⃣ : Ouvrir le Projet

1. Lancez Android Studio
2. Cliquez sur **"Open"**
3. Naviguez jusqu'au dossier `NotesApp` que vous avez téléchargé
4. Sélectionnez-le et cliquez sur **OK**
5. Attendez quelques minutes que Gradle synchronise tout (vous verrez "Gradle Sync" en bas)

Si vous voyez une notification rouge "Gradle Sync Failed", cliquez sur **"Try Again"**.

## Étape 3️⃣ : Compiler l'APK

### Option A : Apk Release (Pour installer sur téléphone) ✅ RECOMMANDÉ

1. Allez à **Build** dans le menu principal
2. Cliquez sur **Build Bundles / APK(s)**
3. Sélectionnez **Build APK(s)**
4. Attendez 2-5 minutes

**Vous verrez un message "Build completed successfully"**

L'APK est maintenant à :
```
NotesApp/app/release/app-release.apk
```

### Option B : Tester sur un émulateur

1. Allez à **Tools** → **Device Manager**
2. Créez un nouveau périphérique virtuel
3. Sélectionnez "Pixel 6" et cliquez sur **Next**
4. Choisissez **Android 14** et **Next**
5. Cliquez sur le bouton ▶️ pour lancer l'émulateur
6. Une fois lancé, allez à **Run** → **Run 'app'**
7. L'app s'installe automatiquement sur l'émulateur

## Étape 4️⃣ : Installer sur votre Téléphone

### Méthode A : Via Câble USB (Plus facile)

1. **Préparez votre téléphone** :
   - Branchez-le en USB
   - Allez dans **Paramètres** → **À propos du téléphone**
   - Trouvez **"Numéro de version"** et appuyez dessus **7 fois**
   - Vous verrez "Mode développeur activé"
   - Retournez à **Paramètres** → **Options développeur**
   - Activez **"Débogage USB"**

2. **Installez via Android Studio** :
   - Tout en gardant l'USB branché
   - Allez à **Run** → **Run 'app'**
   - Sélectionnez votre téléphone dans la liste
   - Android Studio installe l'app automatiquement
   - L'app se lance sur votre téléphone !

### Méthode B : Via Fichier APK (Manuel)

1. **Trouvez l'APK** :
   - Sur votre ordinateur, naviguez à `NotesApp/app/release/`
   - Cherchez le fichier `app-release.apk`

2. **Transférez vers le téléphone** :
   - Connectez le téléphone en USB en mode transfert de fichiers
   - Copiez `app-release.apk` vers la racine du téléphone ou une carte SD
   - Débranchez le câble

3. **Installez sur le téléphone** :
   - Ouvrez le **Gestionnaire de fichiers** sur le téléphone
   - Naviguez jusqu'à l'APK
   - Appuyez dessus
   - Si demandé, allez à **Paramètres** → **Sécurité**
   - Activez **"Sources inconnues"** pour cette application
   - Cliquez sur **Installer**
   - Attendez la fin et appuyez sur **Ouvrir**

## Étape 5️⃣ : Configuration Initiale

À la première ouverture :

1. **Créez un code PIN** (4-8 chiffres)
   - Mémorisez-le bien, il ne peut pas être récupéré !
   - Confirmez-le en le rentrant une deuxième fois

2. **Ajoutez votre empreinte** (optionnel mais recommandé)
   - Cliquez sur **Activer**
   - Placez votre doigt sur le capteur du téléphone
   - Répétez 3-4 fois pour enregistrer

3. **Voilà !** L'app est prête à utiliser 🎉

## 🐛 Dépannage

### "Gradle Sync Failed"
```
Solution : File → Sync Now
           ou Build → Clean Project
```

### "Erreur de compilation"
```
Solution : Build → Clean Project
           Build → Rebuild Project
```

### "APK introuvable après compilation"
```
Vérifiez que vous êtes en mode "Release" et pas "Debug"
L'APK est dans app/release/ et non app/debug/
```

### "Installation échouée sur le téléphone"
```
1. Désinstallez la version précédente
2. Nettoyez le cache : Paramètres → Apps → Secure Notes → Stockage → Vider le cache
3. Réessayez l'installation
```

### "Code PIN oublié"
```
⚠️ Impossible à récupérer (par sécurité)
Vous devrez désinstaller et réinstaller l'app
```

## 📚 Fichier Structure

```
NotesApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/securenotes/app/
│   │   │   │   ├── data/          (modèles de données)
│   │   │   │   ├── repository/    (logique métier)
│   │   │   │   ├── security/      (biométrie, PIN)
│   │   │   │   ├── ui/            (interface)
│   │   │   │   ├── viewmodel/     (gestion d'état)
│   │   │   │   └── MainActivity.kt
│   │   │   ├── res/               (ressources)
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## ✅ Checklist avant de compiler

- [ ] Android Studio installé
- [ ] SDK 34 installé
- [ ] Projet synchronisé sans erreurs
- [ ] Aucun avertissement rouge en gras
- [ ] Vous avez testé dans l'émulateur (recommandé)

## 🎓 Prochaines étapes

Une fois l'app installée :

1. **Explorez les fonctionnalités** :
   - Créez quelques notes
   - Ajoutez des tags et des images
   - Testez la zone secrète (swipe du haut)

2. **Personnalisez** :
   - Changez les couleurs des notes
   - Créez des checklists
   - Utilisez les différents tags

3. **Sécurisez vos notes** :
   - Déplacez les notes sensibles vers la zone secrète
   - Utilisez l'empreinte pour un accès rapide

## 📞 Besoin d'aide ?

Si vous rencontrez des problèmes :
1. Vérifiez les solutions dans la section "Dépannage"
2. Consultez le fichier README.md
3. Les messages d'erreur dans Android Studio sont généralement explicites

---

**Bonne chance et profitez de votre app sécurisée ! 🔒**
