package com.example.todoapp.addedittask;

import com.example.todoapp.data.Task;
import com.example.todoapp.data.source.TasksDataSource;
import com.example.todoapp.data.source.TasksRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddEditTaskPresenterTest {

    @Mock
    private TasksRepository mTasksRepository;

    @Mock
    private AddEditTaskContract.View mAddEditTaskView;

    @Captor
    private ArgumentCaptor<TasksDataSource.GetTaskCallback> mGetTaskCallbackCaptor;

    private AddEditTaskPresenter mAddEditTaskPresenter;

    @Before
    public void setupMocksAndView() {
        MockitoAnnotations.initMocks(this);
        when(mAddEditTaskView.isActive()).thenReturn(true);
    }

    @Test
    public void createPresenterAndSetsThePresenterToView(){
        mAddEditTaskPresenter = new AddEditTaskPresenter(
                null, mTasksRepository, mAddEditTaskView, true);
        verify(mAddEditTaskView).setPresenter(mAddEditTaskPresenter);
    }

    @Test
    public void saveNewTaskToRepositoryAndShowsSuccessMessageUi() {
        mAddEditTaskPresenter = new AddEditTaskPresenter(
                null, mTasksRepository, mAddEditTaskView, true);
        mAddEditTaskPresenter.saveTask("New Task Title", "Some Task Description");
        verify(mTasksRepository).saveTask(any(Task.class));
        verify(mAddEditTaskView).showTasksList();
    }

    @Test
    public void saveTaskAndEmptyTaskShowsErrorUi() {
        mAddEditTaskPresenter = new AddEditTaskPresenter(
                null, mTasksRepository, mAddEditTaskView, true);
        mAddEditTaskPresenter.saveTask("", "");
        verify(mAddEditTaskView).showEmptyTaskError();
    }

    @Test
    public void saveExistingTaskToRepositoryAndShowsSuccessMessageUi() {
        mAddEditTaskPresenter = new AddEditTaskPresenter(
                "1", mTasksRepository, mAddEditTaskView, true);
        mAddEditTaskPresenter.saveTask("Existing Task Title", "Some Task Description");
        verify(mTasksRepository).saveTask(any(Task.class));
        verify(mAddEditTaskView).showTasksList();
    }

    @Test
    public void populateTaskAndCallsRepoAndUpdatesView() {
        Task testTask = new Task("TITLE", "DESCRIPTION");
        mAddEditTaskPresenter = new AddEditTaskPresenter(testTask.getId(),
                mTasksRepository, mAddEditTaskView, true);
        mAddEditTaskPresenter.populateTask();
        verify(mTasksRepository).getTask(eq(testTask.getId()), mGetTaskCallbackCaptor.capture());
        assertThat(mAddEditTaskPresenter.isDataMissing(), is(true));
        mGetTaskCallbackCaptor.getValue().onTaskLoaded(testTask);
        assertNotNull(testTask.getTitle());
        assertNotNull(testTask.getDescription());
        verify(mAddEditTaskView).setTitle(testTask.getTitle());
        verify(mAddEditTaskView).setDescription(testTask.getDescription());
        assertThat(mAddEditTaskPresenter.isDataMissing(), is(false));
    }
    
}
