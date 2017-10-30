package com.example.todoapp.tasks;

import com.example.todoapp.data.Task;
import com.example.todoapp.data.source.TasksDataSource.LoadTasksCallback;
import com.example.todoapp.data.source.TasksRepository;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TasksPresenterTest {

    private static List<Task> TASKS;

    @Mock
    private TasksRepository mTasksRepository;
    @Mock
    private TasksContract.View mTasksView;

    @Captor
    private ArgumentCaptor<LoadTasksCallback> mLoadTasksCallbackCaptor;
    @Captor
    private ArgumentCaptor<List<Task>> mShowTasksArgumentCaptor;

    private TasksPresenter mTasksPresenter;

    @Before
    public void setupTasksPresenter() {
        MockitoAnnotations.initMocks(this);
        mTasksPresenter = new TasksPresenter(mTasksRepository, mTasksView);
        when(mTasksView.isActive()).thenReturn(true);
        Task task1 = new Task("Title1", "Description1");
        Task task2 = new Task("Title2", "Description2", true);
        Task task3 = new Task("Title3", "Description3", true);
        TASKS = Lists.newArrayList(task1, task2, task3);
    }

    @Test
    public void createPresenterAndSetsThePresenterToView() {
        mTasksPresenter = new TasksPresenter(mTasksRepository, mTasksView);
        verify(mTasksView).setPresenter(mTasksPresenter);
    }

    @Test
    public void loadAllTasksFromRepositoryAndLoadIntoView() {
        mTasksPresenter.setFiltering(TasksFilterType.ALL_TASKS);
        mTasksPresenter.loadTasks(true);
        verify(mTasksRepository).getTasks(mLoadTasksCallbackCaptor.capture());
        mLoadTasksCallbackCaptor.getValue().onTasksLoaded(TASKS);
        InOrder inOrder = inOrder(mTasksView);
        inOrder.verify(mTasksView).setLoadingIndicator(true);
        inOrder.verify(mTasksView).setLoadingIndicator(false);
        verify(mTasksView).showTasks(mShowTasksArgumentCaptor.capture());
        assertTrue(mShowTasksArgumentCaptor.getValue().size() == 3);
    }

    @Test
    public void loadActiveTasksFromRepositoryAndLoadIntoView() {
        mTasksPresenter.setFiltering(TasksFilterType.ACTIVE_TASKS);
        mTasksPresenter.loadTasks(true);
        verify(mTasksRepository).getTasks(mLoadTasksCallbackCaptor.capture());
        mLoadTasksCallbackCaptor.getValue().onTasksLoaded(TASKS);
        verify(mTasksView).setLoadingIndicator(false);
        verify(mTasksView).showTasks(mShowTasksArgumentCaptor.capture());
        assertTrue(mShowTasksArgumentCaptor.getValue().size() == 1);
    }

    @Test
    public void loadCompletedTasksFromRepositoryAndLoadIntoView() {
        mTasksPresenter.setFiltering(TasksFilterType.COMPLETED_TASKS);
        mTasksPresenter.loadTasks(true);
        verify(mTasksRepository).getTasks(mLoadTasksCallbackCaptor.capture());
        mLoadTasksCallbackCaptor.getValue().onTasksLoaded(TASKS);
        verify(mTasksView).setLoadingIndicator(false);
        verify(mTasksView).showTasks(mShowTasksArgumentCaptor.capture());
        assertTrue(mShowTasksArgumentCaptor.getValue().size() == 2);
    }

    @Test
    public void clickOnFabAndShowsAddTaskUi() {
        mTasksPresenter.addNewTask();
        verify(mTasksView).showAddTask();
    }

    @Test
    public void clickOnTaskAndShowsDetailUi() {
        Task requestedTask = new Task("Details Requested", "For this task");
        mTasksPresenter.openTaskDetails(requestedTask);
        verify(mTasksView).showTaskDetailsUi(any(String.class));
    }

    @Test
    public void completeTaskAndShowsTaskMarkedComplete() {
        Task task = new Task("Details Requested", "For this task");
        mTasksPresenter.completeTask(task);
        verify(mTasksRepository).completeTask(task);
        verify(mTasksView).showTaskMarkedComplete();
    }

    @Test
    public void activateTaskAndShowsTaskMarkedActive() {
        Task task = new Task("Details Requested", "For this task", true);
        mTasksPresenter.loadTasks(true);
        mTasksPresenter.activateTask(task);
        verify(mTasksRepository).activateTask(task);
        verify(mTasksView).showTaskMarkedActive();
    }

    @Test
    public void unavailableTasksAndShowsError() {
        mTasksPresenter.setFiltering(TasksFilterType.ALL_TASKS);
        mTasksPresenter.loadTasks(true);
        verify(mTasksRepository).getTasks(mLoadTasksCallbackCaptor.capture());
        mLoadTasksCallbackCaptor.getValue().onDataNotAvailable();
        verify(mTasksView).showLoadingTasksError();
    }

}
