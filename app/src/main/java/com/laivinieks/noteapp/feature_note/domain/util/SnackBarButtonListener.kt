package com.laivinieks.noteapp.feature_note.domain.util

import android.view.View

class SnackBarButtonListener(private val function: (string: String) -> Unit) : View.OnClickListener {

    override fun onClick(v: View) {
        function(Constants.UNDO_SNACKBAR)
    }
}