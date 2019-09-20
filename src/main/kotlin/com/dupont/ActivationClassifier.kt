package com.dupont

/**
 * These values were captured by sampling the data and determining which "data" recordings only ever appeared
 * in frames labeled "ACTIVATION".  After sorting the recorded data by timestamp, it became clear that activation
 * periods last 6 frames, and that these special values only appear once per period.  Thus, we can
 */
private val triggerValues = setOf(14783, -11420, 15687, -14120)

class ActivationClassifier(private val activationCallback: () -> Unit) {
    fun onDataRead(reading: Reading) {
        if (triggerValues.contains(reading.data)) {
            activationCallback()
        }
    }
}