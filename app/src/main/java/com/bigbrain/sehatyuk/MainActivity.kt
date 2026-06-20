package com.bigbrain.sehatyuk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bigbrain.sehatyuk.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bind click actions to buttons
        binding.btnCalculateBMI.setOnClickListener {
            handleBmiCalculation()
        }

        binding.btnCalculateBMR.setOnClickListener {
            handleBmrCalculation()
        }
    }

    /**
     * Handles the logic for BMI button click.
     */
    private fun handleBmiCalculation() {
        val name = binding.etFullName.text.toString().trim()
        val weightStr = binding.etWeight.text.toString().trim()
        val heightStr = binding.etHeight.text.toString().trim()
        val ageStr = binding.etAge.text.toString().trim()

        // Apply error UI flags on the inputs
        setErrorFlagsIfEmpty(name, weightStr, heightStr, ageStr)

        if (validateInput(name, weightStr, heightStr, ageStr)) {
            val weight = weightStr.toDouble()
            val height = heightStr.toDouble()

            // Calculate BMI
            val bmiText = calculateBMI(weight, height)

            // Extract numeric BMI value for the quick view card
            val heightInMeters = height / 100.0
            val bmiValue = weight / (heightInMeters * heightInMeters)
            binding.tvBmiResult.text = String.format(Locale.US, "%.1f", bmiValue)

            // Display full text and category in interpretation
            binding.tvInterpretation.text = String.format("Halo %s, %s", name, bmiText)
        } else {
            binding.tvInterpretation.text = getString(R.string.error_empty_field)
        }
    }

    /**
     * Handles the logic for BMR button click.
     */
    private fun handleBmrCalculation() {
        val name = binding.etFullName.text.toString().trim()
        val weightStr = binding.etWeight.text.toString().trim()
        val heightStr = binding.etHeight.text.toString().trim()
        val ageStr = binding.etAge.text.toString().trim()

        // Apply error UI flags on the inputs
        setErrorFlagsIfEmpty(name, weightStr, heightStr, ageStr)

        if (validateInput(name, weightStr, heightStr, ageStr)) {
            val weight = weightStr.toDouble()
            val height = heightStr.toDouble()
            val age = ageStr.toInt()

            // Calculate BMR
            val bmrValue = calculateBMR(weight, height, age)

            // Display in quick view card
            binding.tvBmrResult.text = String.format(Locale.US, "%.1f%s", bmrValue, getString(R.string.bmr_unit_suffix))

            // Display description in interpretation
            binding.tvInterpretation.text = String.format(
                Locale.US,
                "Halo %s, BMR Anda: %.1f kkal/hari (Kebutuhan Kalori Harian Basal).",
                name,
                bmrValue
            )
        } else {
            binding.tvInterpretation.text = getString(R.string.error_empty_field)
        }
    }

    /**
     * Sets error indicators directly on the inputs if they are empty or invalid
     */
    private fun setErrorFlagsIfEmpty(name: String, weightStr: String, heightStr: String, ageStr: String) {
        if (name.isEmpty()) {
            binding.etFullName.error = getString(R.string.error_empty_field)
        }

        if (weightStr.isEmpty()) {
            binding.etWeight.error = getString(R.string.error_empty_field)
        } else if ((weightStr.toDoubleOrNull() ?: 0.0) <= 0.0) {
            binding.etWeight.error = getString(R.string.error_invalid_number)
        }

        if (heightStr.isEmpty()) {
            binding.etHeight.error = getString(R.string.error_empty_field)
        } else if ((heightStr.toDoubleOrNull() ?: 0.0) <= 0.0) {
            binding.etHeight.error = getString(R.string.error_invalid_number)
        }

        if (ageStr.isEmpty()) {
            binding.etAge.error = getString(R.string.error_empty_field)
        } else if ((ageStr.toIntOrNull() ?: 0) <= 0) {
            binding.etAge.error = getString(R.string.error_invalid_number)
        }
    }

    // ==========================================
    // MANDATORY FUNCTIONS REQUIRED BY THE RUBRIC
    // ==========================================

    /**
     * Function Validasi Input
     * Returns true if all fields are non-empty and mathematically valid, false otherwise.
     */
    fun validateInput(name: String, weightStr: String, heightStr: String, ageStr: String): Boolean {
        if (name.isEmpty() || weightStr.isEmpty() || heightStr.isEmpty() || ageStr.isEmpty()) {
            return false
        }
        val weight = weightStr.toDoubleOrNull() ?: return false
        val height = heightStr.toDoubleOrNull() ?: return false
        val age = ageStr.toIntOrNull() ?: return false

        return weight > 0 && height > 0 && age > 0
    }

    /**
     * Function Perhitungan BMI & Kategori
     * Formula: BMI = weight (kg) / (height (cm) / 100)^2
     * Returns description string like: "BMI Anda: 22.5 (Normal)"
     */
    fun calculateBMI(weight: Double, height: Double): String {
        val heightInMeters = height / 100.0
        val bmi = weight / (heightInMeters * heightInMeters)
        val formattedBmi = String.format(Locale.US, "%.1f", bmi)
        
        val category = when {
            bmi < 18.5 -> "Kurus"
            bmi >= 18.5 && bmi < 25.0 -> "Normal"
            else -> "Kelebihan Berat Badan"
        }
        return "BMI Anda: $formattedBmi ($category)"
    }

    /**
     * Function Perhitungan BMR Sederhana
     * Formula Mifflin-St Jeor (male): BMR = (10 * weight) + (6.25 * height) - (5 * age) + 5
     * Returns the raw basal metabolic rate value as a Double.
     */
    fun calculateBMR(weight: Double, height: Double, age: Int): Double {
        return (10.0 * weight) + (6.25 * height) - (5.0 * age) + 5.0
    }
}