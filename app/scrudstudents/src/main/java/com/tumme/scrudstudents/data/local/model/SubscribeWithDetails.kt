package com.tumme.scrudstudents.data.local.model

// Classe de données pour afficher les inscriptions avec les noms complets (challenge du PDF).
data class SubscribeWithDetails(
    val studentId: Int, // ID de l'étudiant.
    val courseId: Int, // ID du cours.
    val score: Float, // Note de l'inscription.
    val studentName: String, // Nom complet de l'étudiant (prénom + nom).
    val courseName: String // Nom du cours.
)