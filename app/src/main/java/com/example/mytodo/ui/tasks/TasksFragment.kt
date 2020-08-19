package com.example.mytodo.ui.tasks

import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytodo.R
import com.example.mytodo.data.model.Task
import com.example.mytodo.ui.base.BaseFragment
import com.example.mytodo.utils.showSnackBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.tasks_frag.*

class TasksFragment private constructor() : BaseFragment(), TasksContract.View {

    override val layoutResource: Int
        get() = R.layout.tasks_frag

    override var isActive: Boolean = false
        get() = isAdded

    override lateinit var presenter: TasksContract.Presenter
    private lateinit var noTasksView: View
    private lateinit var noTaskIcon: ImageView
    private lateinit var noTaskMainView: TextView
    private lateinit var noTaskAddView: TextView
    private lateinit var tasksView: LinearLayout
    private lateinit var filteringLabelView: TextView

    private lateinit var listAdapter: TaskAdapter

    /**
     * Listener for clicks on tasks in the ListView.
     */
    internal var itemListener: TaskItemListener = object : TaskItemListener {
        override fun onTaskClick(clickedTask: Task) {
            presenter.openTaskDetails(clickedTask)
        }

        override fun onCompleteTaskClick(completedTask: Task) {
            presenter.completeTask(completedTask)
        }

        override fun onActivateTaskClick(activatedTask: Task) {
            presenter.activateTask(activatedTask)
        }

    }

    override fun initComponents() {
        listAdapter = TaskAdapter(itemListener)

        tasks_list.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
        }

        // Set up progress indicator
        refresh_layout.apply {
            setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                ContextCompat.getColor(requireContext(), R.color.colorAccent),
                ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
            )
            // Set the scrolling view in the custom SwipeRefreshLayout
            scrollUpChild = tasks_list

            setOnRefreshListener {
                presenter.loadTasks(false)
            }

            filteringLabelView = findViewById(R.id.filteringLabel)
            tasksView = findViewById(R.id.tasksLL)

            // Set up no tasks view
            noTasksView = findViewById(R.id.noTasks)
            noTaskIcon = findViewById(R.id.noTasksIcon)
            noTaskMainView = findViewById(R.id.noTasksMain)
            noTaskAddView = findViewById<TextView>(R.id.noTasksAdd).also {
                it.setOnClickListener { showAddTask() }
            }
        }

        // Set up floating action button
        requireActivity().findViewById<FloatingActionButton>(R.id.fab_add_task).apply {
            setImageResource(R.drawable.ic_add)
            setOnClickListener { presenter.addNewTask() }
        }
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.tasks_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_clear -> presenter.clearCompletedTasks()
            R.id.menu_filter -> showFilteringPopUpMenu()
            R.id.menu_refresh -> presenter.loadTasks(true)
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.result(requestCode, resultCode)
    }


    override fun setLoadingIndicator(active: Boolean) {
        with(refresh_layout) {
            post { isRefreshing = active }
        }
    }

    override fun showTasks(tasks: List<Task>) {
        Log.d("Order ", "TasksFragment showTasks ${tasks.size}")
        listAdapter.updateData(tasks)
        tasksView.visibility = View.VISIBLE
        noTasksView.visibility = View.GONE
    }

    override fun showAddTask() {

        // Intent Add/Edit Task Activity
    }

    override fun showTaskDetailsUi(taskId: String) {

        // In it's own Activity, since it makes more sense that way
        // it gives us the flexibility to show some Intent stubbing
    }

    override fun showTaskMarkedComplete() {
        showMessage(getString(R.string.task_marked_complete))
    }

    override fun showTaskMarkedActive() {
        showMessage(getString(R.string.task_marked_active))
    }

    override fun showCompletedTasksCleared() {
        showMessage(getString(R.string.completed_tasks_cleared))
    }

    override fun showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error))
    }

    override fun showNoTasks() {
        showNoTasksViews(resources.getString(R.string.no_tasks_all), R.drawable.ic_assignment_turned_in_24dp, false)
    }

    override fun showActiveFilterLabel() {
        filteringLabelView.text = resources.getString(R.string.label_active)
    }

    override fun showCompletedFilterLabel() {
        filteringLabelView.text = resources.getString(R.string.label_completed)
    }

    override fun showAllFilterLabel() {
        filteringLabelView.text = resources.getString(R.string.label_all)
    }

    override fun showNoActiveTasks() {
        showNoTasksViews(resources.getString(R.string.no_tasks_active), R.drawable.ic_check_circle_24dp,false)
    }

    override fun showNoCompletedTasks() {
        showNoTasksViews(resources.getString(R.string.no_tasks_completed), R.drawable.ic_verified_user_24dp, false)
    }

    override fun showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message))
    }

    override fun showFilteringPopUpMenu() {
        val activity = activity ?: return
        val context = context ?: return
        androidx.appcompat.widget.PopupMenu(context, activity.findViewById(R.id.menu_filter)).apply {
            menuInflater.inflate(R.menu.filter_tasks, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.active -> presenter.currentFiltering = TasksFilterType.ACTIVE_TASKS
                    R.id.completed -> presenter.currentFiltering = TasksFilterType.COMPLETED_TASKS
                    else -> presenter.currentFiltering = TasksFilterType.ALL_TASKS
                }
                presenter.loadTasks(false)
                true
            }
            show()
        }
    }

    private fun showNoTasksViews(mainText: String, iconRes: Int, showAddView: Boolean) {

        tasksView.visibility = View.GONE
        noTasksView.visibility = View.VISIBLE

        noTaskMainView.text = mainText
        noTaskIcon.setImageResource(iconRes)
        noTaskAddView.visibility = if (showAddView) View.VISIBLE else View.GONE
    }

    private fun showMessage(message: String) {
        view?.showSnackBar(message, Snackbar.LENGTH_LONG)
    }

    interface TaskItemListener {

        fun onTaskClick(clickedTask: Task)

        fun onCompleteTaskClick(completedTask: Task)

        fun onActivateTaskClick(activatedTask: Task)

    }

    companion object {
        fun newInstance() = TasksFragment()
    }
}

