package com.example.data

object RvsEngine {

    data class ModifierItem(val name: String, val value: Double)

    data class RvsResult(
        val basicScore: Double,
        val modifiers: List<ModifierItem>,
        val finalScore: Double,
        val damageGrade: String,
        val riskLevel: String,
        val recommendation: String,
        val retrofitPriority: String
    )

    fun calculate(
        structuralSystem: String, // URM, CM, Stone Masonry, C3
        yearBuilt: Int,
        numberOfStories: Int,
        structuralType: String, // Non Engineered, Semi Engineered, Engineered
        roofType: String,       // Wooden Truss, Steel Truss, RCC, Timber
        foundationType: String, // Plain Concrete, Reinforced Concrete, Stone Masonry
        seismicBand: String,    // None, Roof, Plinth, Lintel, Roof + Plinth, Roof + Lintel
        drawingsAvailable: Boolean,
        morphology: String,     // Horizontal, Mild Slope, Steep Slope
        planIrregularities: String, // Nil, Torsion, Re-entrant Corners, Non-Parallel System
        verticalIrregularities: String, // Nil, Sloping site, Soft Story, Short Column
        exteriorFallingHazard: String,  // Nil, Parapets
        visualCondition: String, // Excellent, Good, Damaged, Collapsed
        cracksPresent: Boolean,
        dampnessPresent: Boolean,
        collapseSignsPresent: Boolean
    ): RvsResult {
        // Basic Score
        val basicScore = when (structuralSystem) {
            "Stone Masonry" -> 0.7
            "URM" -> 0.8
            "C3" -> 1.2
            "CM" -> 1.5
            else -> 1.0
        }

        val modifiersList = mutableListOf<ModifierItem>()

        // 1. Building age > 30 years: -0.2
        val age = 2026 - yearBuilt
        if (age > 30) {
            modifiersList.add(ModifierItem("Building Age > 30 years ($age yrs)", -0.2))
        }

        // 2. Stories > 2: -0.2
        if (numberOfStories > 2) {
            modifiersList.add(ModifierItem("Stories > 2 ($numberOfStories stories)", -0.2))
        }

        // 3. Structural Type
        if (structuralType == "Non Engineered") {
            modifiersList.add(ModifierItem("Non Engineered Building", -0.2))
        } else if (structuralType == "Semi Engineered") {
            modifiersList.add(ModifierItem("Semi Engineered Building", -0.1))
        } else if (structuralType == "Engineered") {
            modifiersList.add(ModifierItem("Engineered Building", 0.2))
        }

        // 4. Visual Condition
        if (visualCondition == "Damaged") {
            modifiersList.add(ModifierItem("Current Condition: Damaged", -0.3))
        } else if (visualCondition == "Collapsed") {
            modifiersList.add(ModifierItem("Current Condition: Collapsed", -0.5))
        } else if (visualCondition == "Excellent") {
            modifiersList.add(ModifierItem("Current Condition: Excellent", 0.1))
        }

        // 5. Cracks present: -0.3
        if (cracksPresent) {
            modifiersList.add(ModifierItem("Cracks Present", -0.3))
        }

        // 6. Dampness present: -0.1
        if (dampnessPresent) {
            modifiersList.add(ModifierItem("Dampness Present", -0.1))
        }

        // 7. Collapse signs: -0.5
        if (collapseSignsPresent) {
            modifiersList.add(ModifierItem("Collapse Signs Present", -0.5))
        }

        // 8. Plan irregularities not Nil: -0.2
        if (planIrregularities != "Nil") {
            modifiersList.add(ModifierItem("Plan Irregularity ($planIrregularities)", -0.2))
        }

        // 9. Vertical irregularities not Nil: -0.2
        if (verticalIrregularities != "Nil") {
            modifiersList.add(ModifierItem("Vertical Irregularity ($verticalIrregularities)", -0.2))
        }

        // 10. Morphology (Mild Slope: -0.1, Steep Slope: -0.1)
        if (morphology == "Mild Slope") {
            modifiersList.add(ModifierItem("Morphology: Mild Slope", -0.1))
        } else if (morphology == "Steep Slope") {
            modifiersList.add(ModifierItem("Morphology: Steep Slope", -0.2))
        }

        // 11. Seismic Band
        if (seismicBand == "None") {
            modifiersList.add(ModifierItem("No Seismic Band", -0.2))
        } else {
            if (seismicBand.contains("Roof")) {
                modifiersList.add(ModifierItem("Seismic Band Component (Roof)", 0.1))
            }
            if (seismicBand.contains("Plinth")) {
                modifiersList.add(ModifierItem("Seismic Band Component (Plinth)", 0.1))
            }
            if (seismicBand.contains("Lintel")) {
                modifiersList.add(ModifierItem("Seismic Band Component (Lintel)", 0.1))
            }
        }

        // 12. Wooden Truss or Timber roof: -0.1
        if (roofType == "Wooden Truss" || roofType == "Timber") {
            modifiersList.add(ModifierItem("Wooden Truss or Timber Roof", -0.1))
        }

        // 13. Plain Concrete foundation: -0.1, Stone Masonry foundation: -0.2, RC foundation: +0.1
        if (foundationType == "Plain Concrete") {
            modifiersList.add(ModifierItem("Plain Concrete Foundation", -0.1))
        } else if (foundationType == "Stone Masonry") {
            modifiersList.add(ModifierItem("Stone Masonry Foundation", -0.2))
        } else if (foundationType == "Reinforced Concrete") {
            modifiersList.add(ModifierItem("Reinforced Concrete Foundation", 0.1))
        }

        // 14. Parapets hazard: -0.1
        if (exteriorFallingHazard == "Parapets") {
            modifiersList.add(ModifierItem("Falling Hazard: Parapets", -0.1))
        }

        // 15. Drawings
        if (drawingsAvailable) {
            modifiersList.add(ModifierItem("Building Drawings Available", 0.1))
        } else {
            modifiersList.add(ModifierItem("No Building Drawings Available", -0.1))
        }

        val modifiersSum = modifiersList.sumOf { it.value }
        val finalScore = (basicScore + modifiersSum).coerceAtLeast(0.0)

        // Compile grade & risk
        val damageGrade: String
        val riskLevel: String
        val recommendation: String

        if (finalScore >= 1.0) {
            damageGrade = "D2-D3"
            riskLevel = "Medium"
            recommendation = "Building may experience light to moderate damage. Regular inspection recommended."
        } else if (finalScore >= 0.5) {
            damageGrade = "D3-D4"
            riskLevel = "High"
            recommendation = "Building may experience moderate to severe damage. Detailed engineering assessment recommended."
        } else {
            damageGrade = "D4-D5"
            riskLevel = "Critical"
            recommendation = "Building may experience severe to total damage. Immediate expert inspection and retrofitting strongly recommended."
        }

        // Retrofit Priority
        // Critical: collapse signs OR score < 0.5
        // High: D3-D4 risk
        // Medium: D2-D3 risk
        val retrofitPriority = if (collapseSignsPresent || finalScore < 0.5) {
            "Critical"
        } else if (riskLevel == "High") {
            "High"
        } else {
            "Medium"
        }

        return RvsResult(
            basicScore = basicScore,
            modifiers = modifiersList,
            finalScore = Math.round(finalScore * 100.0) / 100.0,
            damageGrade = damageGrade,
            riskLevel = riskLevel,
            recommendation = recommendation,
            retrofitPriority = retrofitPriority
        )
    }
}
