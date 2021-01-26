package doa.ai.utils.adapter

import android.content.Context
import android.os.Build
import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.view.ActionMode

import java.util.ArrayList

import doa.ai.R
import doa.ai.main.notes.bplan.plan.AdapterBPlan
import doa.ai.main.notes.bplan.plan.PlanListResult

/**
 * Created by SONU on 22/03/16.
 */
class Toolbar_ActionMode_Callback
//   /// private ArrayList<Item_Model> data;
//    private boolean isListViewFragment;


(//
        private val context: Context, private val adapterBPlan: AdapterBPlan, private val data: ArrayList<PlanListResult>) : ActionMode.Callback {

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        mode.menuInflater.inflate(R.menu.menu_pinned, menu)//Inflate the menu over action mode
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {

        //Sometimes the meu will not be visible so for that we need to set their visibility manually in this method
        //So here show action menu according to SDK Levels
        if (Build.VERSION.SDK_INT < 11) {
            //            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_delete), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            //            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_copy), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            //            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_forward), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            //        } else {
            //            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            //            menu.findItem(R.id.action_copy).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            //            menu.findItem(R.id.action_forward).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_add_pinned ->{

            }

        }
        return false
    }


    override fun onDestroyActionMode(mode: ActionMode) {


    }
}
