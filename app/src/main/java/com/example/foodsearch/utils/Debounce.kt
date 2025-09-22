package com.example.foodsearch.utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


import kotlinx.coroutines.*

/**
 * Function that delays execution of actions based on a specified delay.
 *
 * @param delayMillis Time interval between consecutive executions.
 * @param coroutineScope Scope where coroutines will run.
 * @param replaceWithLatest Whether to cancel any ongoing job and process only the latest parameter.
 * @param action Action to perform after the delay.
 */
fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    replaceWithLatest: Boolean = false,
    action: suspend (T) -> Unit
): (T) -> Unit {
    var job: Job? = null

    return { param: T ->
        coroutineScope.launch {
            try {
                // Cancel previous job if it exists and we want to process only the latest event
                if (job?.isActive == true && replaceWithLatest) {
                    job!!.cancel()
                }

                // Start a new job
                job = launch {
                    delay(delayMillis)
                    action(param)
                }
            } catch (e: CancellationException) {
                // Handle cancellation properly
            }
        }
    }
}