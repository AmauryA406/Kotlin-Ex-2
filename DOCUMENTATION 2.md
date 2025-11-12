# Labwork 1 II3510 - Documentation

**Étudiant :** Amaury Allemand  
**Date :** 05/11/2025  
**Sujet :** Application de gestion universitaire multi-rôles (Student/Teacher)
**GitHub Link :** https://github.com/AmauryA406/Kotlin-Ex-2

---

## 1. Présentation du projet

### 1.1 Objectif

Développer une application Android permettant la gestion d'inscriptions universitaires avec deux types d'utilisateurs :

- **Étudiants** : consultation de cours, inscriptions, visualisation des notes et calcul de moyenne pondérée
- **Enseignants** : déclaration de cours, gestion d'étudiants, saisie de notes

### 1.2 Fonctionnalités principales

#### Authentification

- Inscription Student/Teacher avec email/password
- Connexion avec redirection selon le rôle
- Déconnexion

#### Parcours Student

- Écran d'accueil avec menu de navigation
- Consultation des cours disponibles (filtrés par niveau)
- Inscription aux cours
- Visualisation des inscriptions
- Consultation des notes
- Calcul automatique de la moyenne pondérée par ECTS

#### Parcours Teacher

- Écran d'accueil avec menu de navigation
- Visualisation de ses cours
- Déclaration de cours (assignation)
- Liste des étudiants par cours
- Saisie/modification des notes

---

## 2. Architecture technique

### 2.1 Architecture MVVM (Model-View-ViewModel)

```
UI Layer (Composables)
    ↓ collectAsState()
ViewModel Layer (StateFlow)
    ↓ suspend calls
Repository Layer
    ↓ DAO calls
Database Layer (Room)
```

### 2.2 Technologies utilisées

- **Langage** : Kotlin
- **UI** : Jetpack Compose (Material Design 3)
- **Architecture** : MVVM
- **Base de données** : Room (SQLite)
- **Injection de dépendances** : Hilt/Dagger
- **Programmation asynchrone** : Kotlin Coroutines + Flow
- **Navigation** : Jetpack Navigation Compose

### 2.3 Structure du projet

```
app/scrudstudents/src/main/java/com/tumme/scrudstudents/
│
├── data/
│   ├── local/
│   │   ├── model/          (Entities)
│   │   │   ├── StudentEntity.kt
│   │   │   ├── TeacherEntity.kt
│   │   │   ├── CourseEntity.kt
│   │   │   ├── SubscribeEntity.kt
│   │   │   ├── Gender.kt, Level.kt, UserRole.kt
│   │   │
│   │   ├── dao/            (Data Access Objects)
│   │   │   ├── StudentDao.kt
│   │   │   ├── TeacherDao.kt
│   │   │   ├── CourseDao.kt
│   │   │   ├── SubscribeDao.kt
│   │   │
│   │   ├── AppDatabase.kt
│   │   └── Converters.kt
│   │
│   └── repository/
│       ├── SCRUDRepository.kt
│       └── AuthRepository.kt
│
├── ui/
│   ├── auth/
│   │   ├── AuthViewModel.kt
│   │   ├── LoginScreen.kt
│   │   └── RegisterScreen.kt
│   │
│   ├── student/
│   │   ├── StudentHomeScreen.kt
│   │   ├── StudentCoursesScreen.kt
│   │   ├── StudentMySubscriptionsScreen.kt
│   │   ├── StudentGradesScreen.kt
│   │   └── StudentFinalGradeScreen.kt
│   │
│   ├── teacher/
│   │   ├── TeacherHomeScreen.kt
│   │   ├── TeacherMyCoursesScreen.kt
│   │   ├── TeacherDeclareCoursesScreen.kt
│   │   ├── TeacherStudentListScreen.kt
│   │   └── TeacherGradeEntryScreen.kt
│   │
│   └── navigation/
│       └── NavGraphs.kt
│
├── di/
│   └── AppModule.kt
│
└── MainActivity.kt
```

---

## 3. Modèle de données

### 3.1 Schéma de base de données

#### Table `students`

- `idStudent` (PK, auto-increment)
- `email` (unique)
- `password`
- `firstName`, `lastName`
- `dateOfBirth`
- `gender` (enum: Male/Female)
- `level` (enum: P1, P2, P3, B1, B2, B3, A1, A2, A3, MS, PhD)

#### Table `teachers`

- `idTeacher` (PK, auto-increment)
- `email` (unique)
- `password`
- `firstName`, `lastName`
- `department`

#### Table `courses`

- `idCourse` (PK, auto-increment)
- `nameCourse`
- `ectsCourse` (coefficient ECTS)
- `levelCourse` (enum)
- `teacherId` (FK nullable vers teachers)

#### Table `subscribes`

- `studentId` (PK, FK vers students)
- `courseId` (PK, FK vers courses)
- `score` (note sur 20, -1 = non noté)

### 3.2 Relations

- **Student ↔ Subscribe** : 1-N
- **Course ↔ Subscribe** : 1-N
- **Teacher ↔ Course** : 1-N (un teacher peut enseigner plusieurs cours)

---

## 4. Fonctionnalités clés implémentées

### 4.1 Authentification (AuthRepository)

**Méthodes :**

- `loginStudent(email, password)` → AuthResult
- `loginTeacher(email, password)` → AuthResult
- `registerStudent(student)` → AuthResult
- `registerTeacher(teacher)` → AuthResult

**Flux :**

1. Utilisateur entre email/password
2. AuthViewModel appelle AuthRepository
3. Vérification en base de données
4. Si succès → Redirection selon le rôle (Student/Teacher)

### 4.2 Calcul de moyenne pondérée

**Formule utilisée :**

```
Moyenne = Σ(note × ECTS) / Σ(ECTS)
```

**Implémentation :**

```kotlin
suspend fun calculateWeightedGrade(studentId: Int, level: Level): Float? {
    val subscriptions = repository.getSubscribesByStudent(studentId)
    var totalWeightedScore = 0f
    var totalEcts = 0f

    for (subscribe in subscriptions) {
        if (subscribe.score >= 0) {
            val course = repository.getCourseById(subscribe.courseId)
            if (course?.levelCourse == level) {
                totalWeightedScore += subscribe.score * course.ectsCourse
                totalEcts += course.ectsCourse
            }
        }
    }

    return if (totalEcts > 0) totalWeightedScore / totalEcts else null
}
```

### 4.3 Navigation dynamique

**Navigation basée sur le rôle :**

- Login réussi → Récupération du UserRole
- Si STUDENT → StudentHomeScreen
- Si TEACHER → TeacherHomeScreen

**Passage de paramètres :**

```kotlin
navController.navigate("student_home/$userId/$level")
```

### 4.4 Gestion des états UI (StateFlow)

**Pattern utilisé :**

```kotlin
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
```

**Observation dans l'UI :**

```kotlin
val authState by viewModel.authState.collectAsState()

when (authState) {
    is AuthState.Loading -> CircularProgressIndicator()
    is AuthState.Error -> Text((authState as AuthState.Error).message)
    is AuthState.Success -> /* Navigation */
}
```

---

## 5. Difficultés rencontrées et solutions

### 5.1 Migration de base de données

**Problème :** Ajout de nouveaux champs (`email`, `password`, `level`) dans StudentEntity cassait la compatibilité.

**Solution :**

- Version de la base passée de 1 à 2
- Utilisation de `.fallbackToDestructiveMigration()` (acceptable en développement)
- Champs avec valeurs par défaut pour compatibilité temporaire

### 5.2 Compatibilité Level vs LevelCourse

**Problème :** Deux enums différents pour le niveau (ancien `LevelCourse`, nouveau `Level`).

**Solution :**

- Converters supportant les deux types
- CourseEntity garde `LevelCourse` pour compatibilité avec anciens écrans
- Nouveaux écrans utilisent `Level`

### 5.3 Filtrage des cours par niveau

**Problème :** Comparaison entre `Level` (nouveau) et `LevelCourse` (ancien).

**Solution :**

```kotlin
val availableCourses = allCourses.filter {
    it.levelCourse.name == studentLevel.value
}
```

### 5.4 Mise à jour des notes en temps réel

**Problème :** Les notes modifiées par Teacher n'apparaissaient pas immédiatement chez Student.

**Solution :** Utilisation de `Flow` dans les DAOs pour observer les changements en temps réel :

```kotlin
@Query("SELECT * FROM subscribes")
fun getAllSubscribes(): Flow<List<SubscribeEntity>>
```

---

## 6. Tests effectués

### 6.1 Scénario Student

✅ Inscription avec niveau B1  
✅ Connexion  
✅ Visualisation des cours disponibles (niveau B1 uniquement)  
✅ Inscription à 3 cours  
✅ Vérification dans "My Subscriptions"  
✅ Vérification "No grades yet" avant notation  
✅ Vérification des notes après saisie par Teacher  
✅ Calcul automatique de la moyenne pondérée  
✅ Déconnexion

### 6.2 Scénario Teacher

✅ Inscription  
✅ Connexion  
✅ Déclaration de 2 cours  
✅ Vérification dans "My Courses"  
✅ Consultation de la liste d'étudiants  
✅ Saisie de notes (15/20, 12/20, 18/20)  
✅ Vérification de la sauvegarde  
✅ Déconnexion

### 6.3 Vérification croisée

✅ Student voit les notes saisies par Teacher  
✅ Moyenne pondérée correcte : (15×6 + 12×5 + 18×4) / (6+5+4) = **14.6/20**  
✅ Navigation fluide sans crash

---

## 7. Améliorations possibles

### Court terme

- Validation email (format)
- Hash des mots de passe (sécurité)
- Messages d'erreur plus explicites
- Confirmation avant suppression

### Moyen terme

- Filtrage avancé (recherche par nom)
- Export PDF des relevés de notes
- Photos de profil
- Gestion de plusieurs niveaux par étudiant
- Statistiques avancées pour Teacher

### Long terme

- Mode Admin (gestion globale)
- Notifications push
- Synchronisation cloud
- Mode hors-ligne

---

## 8. Conclusion

Ce projet a permis de mettre en pratique les concepts clés du développement Android moderne :

**Acquis techniques :**

- Architecture MVVM complète
- Maîtrise de Room (relations, migrations, Flow)
- Injection de dépendances avec Hilt
- Navigation multi-niveaux avec Jetpack Compose
- Gestion d'états réactifs (StateFlow)

**Compétences développées :**

- Conception d'architecture scalable
- Gestion de base de données relationnelle
- UX/UI avec Material Design 3
- Débogage et résolution de problèmes

L'application est **fonctionnelle** et répond au cahier des charges. Les deux parcours utilisateurs (Student et Teacher) sont complets et interconnectés, permettant un workflow réaliste de gestion universitaire.

---

## Annexes

### A. Captures d'écran

[À ajouter : screenshots de LoginScreen, StudentHome, TeacherHome, GradeEntry, etc.]

### B. Instructions d'installation

1. Cloner le repository
2. Ouvrir avec Android Studio
3. Sync Gradle
4. Lancer sur émulateur ou appareil physique
5. (Optionnel) Initialiser les données de test via InitDataScreen

### C. Dépendances principales

```gradle
// Room
implementation "androidx.room:room-runtime:2.5.0"
kapt "androidx.room:room-compiler:2.5.0"
implementation "androidx.room:room-ktx:2.5.0"

// Hilt
implementation "com.google.dagger:hilt-android:2.44"
kapt "com.google.dagger:hilt-compiler:2.44"

// Navigation
implementation "androidx.navigation:navigation-compose:2.7.0"

// Jetpack Compose
implementation platform('androidx.compose:compose-bom:2023.08.00')
```

---

**Fin de la documentation**
