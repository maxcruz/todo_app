package com.example.todoapp.taskdetail;

import com.example.todoapp.data.Task;
import com.example.todoapp.data.source.TasksDataSource;
import com.example.todoapp.data.source.TasksRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskDetailPresenterTest {

    private static final String TITLE_TEST = "title";
    private static final String DESCRIPTION_TEST = "description";
    private static final Task ACTIVE_TASK = new Task(TITLE_TEST, DESCRIPTION_TEST);
    private static final Task COMPLETED_TASK = new Task(TITLE_TEST, DESCRIPTION_TEST, true);

    @Mock
    private TasksRepository mTasksRepository;

    @Mock
    private TaskDetailContract.View mTaskDetailView;

    @Captor
    private ArgumentCaptor<TasksDataSource.GetTaskCallback> mGetTaskCallbackCaptor;

    private TaskDetailPresenter mTaskDetailPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mTaskDetailView.isActive()).thenReturn(true);
    }

    @Test
    public void createPresenterAndSetsThePresenterToView() {
        mTaskDetailPresenter = new TaskDetailPresenter(
                ACTIVE_TASK.getId(), mTasksRepository, mTaskDetailView);
        verify(mTaskDetailView).setPresenter(mTaskDetailPresenter);
    }

    @Test
    public void getActiveTaskFromRepositoryAndLoadIntoView() {
        mTaskDetailPresenter = new TaskDetailPresenter(
                ACTIVE_TASK.getId(), mTasksRepository, mTaskDetailView);
        mTaskDetailPresenter.start();
        verify(mTasksRepository).getTask(eq(ACTIVE_TASK.getId()), mGetTaskCallbackCaptor.capture());
        InOrder inOrder = inOrder(mTaskDetailView);
        inOrder.verify(mTaskDetailView).setLoadingIndicator(true);
        mGetTaskCallbackCaptor.getValue().onTaskLoaded(ACTIVE_TASK);
        inOrder.verify(mTaskDetailView).setLoadingIndicator(false);
        verify(mTaskDetailView).showTitle(TITLE_TEST);
        verify(mTaskDetailView).showDescription(DESCRIPTION_TEST);
        verify(mTaskDetailView).showCompletionStatus(false);
    }

    @Test
    public void getCompletedTaskFromRepositoryAndLoadIntoView() {
        mTaskDetailPresenter = new TaskDetailPresenter(COMPLETED_TASK.getId(), mTasksRepository, mTaskDetailView);
        mTaskDetailPresenter.start();
        verify(mTasksRepository).getTask(eq(COMPLETED_TASK.getId()), mGetTaskCallbackCaptor.capture());
        InOrder inOrder = inOrder(mTaskDetailView);
        inOrder.verify(mTaskDetailView).setLoadingIndicator(true);
        mGetTaskCallbackCaptor.getValue().onTaskLoaded(COMPLETED_TASK);
        inOrder.verify(mTaskDetailView).setLoadingIndicator(false);
        verify(mTaskDetailView).showTitle(TITLE_TEST);
        verify(mTaskDetailView).showDescription(DESCRIPTION_TEST);
        verify(mTaskDetailView).showCompletionStatus(true);
    }

    @Test
    public void deleteTask() {
        Task task = new Task(TITLE_TEST, DESCRIPTION_TEST);
        mTaskDetailPresenter = new TaskDetailPresenter(task.getId(), mTasksRepository, mTaskDetailView);
        mTaskDetailPresenter.deleteTask();
        verify(mTasksRepository).deleteTask(task.getId());
        verify(mTaskDetailView).showTaskDeleted();
    }

    @Test
    public void completeTask() {
        Task task = new Task(TITLE_TEST, DESCRIPTION_TEST);
        mTaskDetailPresenter = new TaskDetailPresenter(task.getId(), mTasksRepository, mTaskDetailView);
        mTaskDetailPresenter.start();
        mTaskDetailPresenter.completeTask();
        verify(mTasksRepository).completeTask(task.getId());
        verify(mTaskDetailView).showTaskMarkedComplete();
    }

    @Test
    public void activateTask() {
        Task task = new Task(TITLE_TEST, DESCRIPTION_TEST, true);
        mTaskDetailPresenter = new TaskDetailPresenter(task.getId(), mTasksRepository, mTaskDetailView);
        mTaskDetailPresenter.start();
        mTaskDetailPresenter.activateTask();
        verify(mTasksRepository).activateTask(task.getId());
        verify(mTaskDetailView).showTaskMarkedActive();
    }

    @Test
    public void activeTaskIsShownWhenEditing() {
        mTaskDetailPresenter = new TaskDetailPresenter(ACTIVE_TASK.getId(), mTasksRepository, mTaskDetailView);
        mTaskDetailPresenter.editTask();
        verify(mTaskDetailView).showEditTask(ACTIVE_TASK.getId());
    }

}
