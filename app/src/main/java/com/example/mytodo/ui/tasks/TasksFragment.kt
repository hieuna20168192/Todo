package com.example.mytodo.ui.tasks

import com.example.mytodo.R
import com.example.mytodo.data.model.Task
import com.example.mytodo.data.source.TasksRepository
import com.example.mytodo.data.source.local.TaskLocalDataSource
import com.example.mytodo.data.source.local.dao.TaskDaoImpl
import com.example.mytodo.data.source.remote.TaskRemoteDataSource
import com.example.mytodo.ui.base.BaseFragment
import com.example.mytodo.utils.showToast
import kotlinx.android.synthetic.main.tasks_frag.*
import kotlinx.android.synthetic.main.tasks_frag.view.*
import kotlinx.android.synthetic.main.tasks_frag.view.tasks_list

class TasksFragment private constructor() : BaseFragment(), TasksContract.View {

    override val layoutResource: Int
        get() = R.layout.tasks_frag

    private var presenter: TasksContract.Presenter? = null

    private val taskAdapter by lazy {
        TaskAdapter()
    }
    override fun initComponents() {
        initRecyclerView()
        // Set Onclick
    }

    private fun initRecyclerView() {
        tasks_list.adapter = taskAdapter
    }

    override fun initData() {
        initPresenter()
        presenter?.start()
    }

    override fun showTasks(tasks: List<Task>) {
        taskAdapter.updateData(tasks)
    }

    override fun toast(obj: Any) {
        context?.showToast(obj)
    }

    private fun initPresenter() {
        val context = context ?: return
        val localDataSource = TaskLocalDataSource.getInstance(TaskDaoImpl.getInstance(context))
        val remoteDataSource = TaskRemoteDataSource
        val repository = TasksRepository.getInstance(localDataSource, remoteDataSource)
        presenter = TasksPresenter(this, repository)
    }

    companion object {
        fun newInstance() = TasksFragment()
    }
}