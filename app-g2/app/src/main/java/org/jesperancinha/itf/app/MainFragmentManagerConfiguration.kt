package org.jesperancinha.itf.app

import android.R
import android.app.AlertDialog
import android.content.Intent
import android.view.View
import org.jesperancinha.itf.app.FileManagerActivity

abstract class MainFragmentManagerConfiguration {
    protected abstract val mainFragment: MainFragment
    protected val editConfiguration: EditConfiguration?
        protected get() = mainFragment.getEditConfiguration()
    protected val controlConfiguration: ControlConfiguration?
        protected get() = mainFragment.getControlConfiguration()
    protected val imageConfiguration: ImageConfiguration?
        protected get() = mainFragment.getImageConfiguration()

    fun checkButtonStart() {
        mainFragment.checkButtonStart()
    }

    val findFileIntent: Intent
        get() = Intent(
            mainFragment.activity,
            org.jesperancinha.itf.app.FileManagerActivity::class.java
        )

    protected fun alertFailedUnicodeParsing() {
        AlertDialog.Builder(mainFragment.activity)
            .setTitle("Error with your code selection")
            .setMessage("Unfortunatelly this Unicode is not supported. If you want a working example, try: LATIN_EXTENDED_A")
            .setIcon(R.drawable.ic_dialog_alert)
            .show()
    }

    protected fun getDestination(view: View): Int {
        if (view === controlConfiguration.getBtnStart()) {
            return 2
        } else if (view === controlConfiguration.getBtnStartEmail()) {
            return 1
        }
        return 0
    }
}