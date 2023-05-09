package com.dflow.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dflow.R
import com.dflow.ui.BindingFragment
import timber.log.Timber

var Activity_BUNDLE_FORWARD_KEY = "BUNDLE_FORWARD_KEY"

object Navigator {


    fun showFragment(supportFragmentManager: FragmentManager, fragment: Fragment, tag: String, addToBackStack: Boolean) {
        try {
            val name: String? = if (addToBackStack) tag else null
            val transaction = supportFragmentManager.beginTransaction()
            supportFragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            transaction.replace(R.id.flow_container, fragment, tag).addToBackStack(name).commit()
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

    fun showActivity(activity: Activity?, activityClass: Class<*>, clearTask: Boolean = false, extras: Bundle? = null) {
        activity?.let {
            val intent = Intent(activity, activityClass)
            if (clearTask) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            activity.getBundleForward()?.let {
                intent.putExtra(Activity_BUNDLE_FORWARD_KEY, it)
            }
            extras?.let {
                intent.putExtras(it)
            }
            activity.startActivity(intent)
        }
    }

    fun getCurrentFragment(supportFragmentManager: FragmentManager): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.flow_container)
    }


    fun showActivityForResult(activityResultLauncher: ActivityResultLauncher<Intent>, activity: Activity?, activityClass: Class<*>, extras: Bundle? = null) {
        activity?.let {
            val intent = Intent(activity, activityClass)
            activity.getBundleForward()?.let {
                intent.putExtra(Activity_BUNDLE_FORWARD_KEY, it)
            }
            extras?.let {
                intent.putExtras(it)
            }
            activityResultLauncher.launch(intent)
        }
    }


    fun showDialog(supportFragmentManager: FragmentManager, dialog: DialogFragment, tag: String) {
        if (!supportFragmentManager.isStateSaved)
            dialog.show(supportFragmentManager, tag)
    }

}

fun Activity.getBundleForward(): Bundle? {
    return intent.getBundleExtra(Activity_BUNDLE_FORWARD_KEY)
}

fun Activity.stopForwarding() {
    intent?.extras?.remove(Activity_BUNDLE_FORWARD_KEY)
}