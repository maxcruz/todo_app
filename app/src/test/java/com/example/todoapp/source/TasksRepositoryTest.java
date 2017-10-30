package com.example.todoapp.source;

import com.example.todoapp.data.Task;
import com.example.todoapp.data.source.TasksDataSource;
import com.example.todoapp.data.source.TasksRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class TasksRepositoryTest {

    private final static String TASK_TITLE = "title";
    private final static String TASK_TITLE2 = "title2";
    private final static String TASK_TITLE3 = "title3";

    private TasksRepository mTasksRepository;

    @Mock
    private TasksDataSource mTasksLocalDataSource;
    @Mock
    private TasksDataSource.GetTaskCallback mGetTaskCallback;
    @Mock
    private TasksDataSource.LoadTasksCallback mLoadTasksCallback;

    @Captor
    private ArgumentCaptor<TasksDataSource.LoadTasksCallback> mTasksCallbackCaptor;
    @Captor
    private ArgumentCaptor<TasksDataSource.GetTaskCallback> mTaskCallbackCaptor;

    @Before
    public void setupTasksRepository() {
        MockitoAnnotations.initMocks(this);
        mTasksRepository = TasksRepository.Companion.getInstance(mTasksLocalDataSource);
    }

    @After
    public void destroyRepositoryInstance() {
        TasksRepository.Companion.clearInstance();
    }

    @Test
    public void getTasksAndRequestsAllTasksFromLocalDataSource() {
        mTasksRepository.getTasks(mLoadTasksCallback);
        verify(mTasksLocalDataSource).getTasks(any(TasksDataSource.LoadTasksCallback.class));
    }

    @Test
    public void saveTaskAndSavesTaskToServiceAPI() {
        Task newTask = new Task(TASK_TITLE, "Some Task Description");
        mTasksRepository.saveTask(newTask);
        verify(mTasksLocalDataSource).saveTask(newTask);
        assertNotNull(mTasksRepository.getCachedTasks());
        assertThat(mTasksRepository.getCachedTasks().size(), is(1));
    }

    @Test
    public void completeTaskAndCompletesTaskToServiceAPIUpdatesCache() {
        Task newTask = new Task(TASK_TITLE, "Some Task Description");
        mTasksRepository.saveTask(newTask);
        mTasksRepository.completeTask(newTask);
        verify(mTasksLocalDataSource).completeTask(newTask);
        assertNotNull(mTasksRepository.getCachedTasks());
        assertThat(mTasksRepository.getCachedTasks().size(), is(1));
        assertThat(mTasksRepository.getCachedTasks().get(newTask.getId()).isActive(), is(false));
    }

    @Test
    public void completeTaskIdAndCompletesTaskToServiceAPIUpdatesCache() {
        Task newTask = new Task(TASK_TITLE, "Some Task Description");
        mTasksRepository.saveTask(newTask);
        mTasksRepository.completeTask(newTask.getId());
        verify(mTasksLocalDataSource).completeTask(newTask);
        assertNotNull(mTasksRepository.getCachedTasks());
        assertThat(mTasksRepository.getCachedTasks().size(), is(1));
        assertThat(mTasksRepository.getCachedTasks().get(newTask.getId()).isActive(), is(false));
    }

    @Test
    public void activateTask_activatesTaskToServiceAPIUpdatesCache() {
        Task newTask = new Task(TASK_TITLE, "Some Task Description", true);
        mTasksRepository.saveTask(newTask);
        mTasksRepository.activateTask(newTask);
        verify(mTasksLocalDataSource).activateTask(newTask);
        assertNotNull(mTasksRepository.getCachedTasks());
        assertThat(mTasksRepository.getCachedTasks().size(), is(1));
        assertThat(mTasksRepository.getCachedTasks().get(newTask.getId()).isActive(), is(true));
    }

    @Test
    public void activateTaskIdAndActivatesTaskToServiceAPIUpdatesCache() {
        Task newTask = new Task(TASK_TITLE, "Some Task Description", true);
        mTasksRepository.saveTask(newTask);
        mTasksRepository.activateTask(newTask.getId());
        verify(mTasksLocalDataSource).activateTask(newTask);
        assertNotNull(mTasksRepository.getCachedTasks());
        assertThat(mTasksRepository.getCachedTasks().size(), is(1));
        assertThat(mTasksRepository.getCachedTasks().get(newTask.getId()).isActive(), is(true));
    }

    @Test
    public void getTaskAndRequestsSingleTaskFromLocalDataSource() {
        mTasksRepository.getTask(TASK_TITLE, mGetTaskCallback);
        verify(mTasksLocalDataSource).getTask(eq(TASK_TITLE), any(TasksDataSource.GetTaskCallback.class));
    }

    @Test
    public void deleteCompletedTasksAndDeleteCompletedTasksToServiceAPIUpdatesCache() {
        Task newTask = new Task(TASK_TITLE, "Some Task Description", true);
        mTasksRepository.saveTask(newTask);
        Task newTask2 = new Task(TASK_TITLE2, "Some Task Description");
        mTasksRepository.saveTask(newTask2);
        Task newTask3 = new Task(TASK_TITLE3, "Some Task Description", true);
        mTasksRepository.saveTask(newTask3);
        mTasksRepository.clearCompletedTasks();
        verify(mTasksLocalDataSource).clearCompletedTasks();
        assertNotNull(mTasksRepository.getCachedTasks());
        assertThat(mTasksRepository.getCachedTasks().size(), is(1));
        assertTrue(mTasksRepository.getCachedTasks().get(newTask2.getId()).isActive());
        assertThat(mTasksRepository.getCachedTasks().get(newTask2.getId()).getTitle(), is(TASK_TITLE2));
    }

    @Test
    public void deleteAllTasksAndDeleteTasksToServiceAPIUpdatesCache() {
        Task newTask = new Task(TASK_TITLE, "Some Task Description", true);
        mTasksRepository.saveTask(newTask);
        Task newTask2 = new Task(TASK_TITLE2, "Some Task Description");
        mTasksRepository.saveTask(newTask2);
        Task newTask3 = new Task(TASK_TITLE3, "Some Task Description", true);
        mTasksRepository.saveTask(newTask3);
        mTasksRepository.deleteAllTasks();
        verify(mTasksLocalDataSource).deleteAllTasks();
        assertNotNull(mTasksRepository.getCachedTasks());
        assertThat(mTasksRepository.getCachedTasks().size(), is(0));
    }

    @Test
    public void deleteTaskAndDeleteTaskToServiceAPIRemovedFromCache() {
        Task newTask = new Task(TASK_TITLE, "Some Task Description", true);
        mTasksRepository.saveTask(newTask);
        assertNotNull(mTasksRepository.getCachedTasks());
        assertThat(mTasksRepository.getCachedTasks().containsKey(newTask.getId()), is(true));
        mTasksRepository.deleteTask(newTask.getId());
        verify(mTasksLocalDataSource).deleteTask(newTask.getId());
        assertThat(mTasksRepository.getCachedTasks().containsKey(newTask.getId()), is(false));
    }

    @Test
    public void getTasksWithBothDataSourcesUnavailableAndFiresOnDataUnavailable() {
        mTasksRepository.getTasks(mLoadTasksCallback);
        setTasksNotAvailable(mTasksLocalDataSource);
        verify(mLoadTasksCallback).onDataNotAvailable();
    }

    @Test
    public void getTaskWithBothDataSourcesUnavailableAndFiresOnDataUnavailable() {
        final String taskId = "123";
        mTasksRepository.getTask(taskId, mGetTaskCallback);
        setTaskNotAvailable(mTasksLocalDataSource, taskId);
        verify(mGetTaskCallback).onDataNotAvailable();
    }

    private void setTasksNotAvailable(TasksDataSource dataSource) {
        verify(dataSource).getTasks(mTasksCallbackCaptor.capture());
        mTasksCallbackCaptor.getValue().onDataNotAvailable();
    }

    private void setTaskNotAvailable(TasksDataSource dataSource, String taskId) {
        verify(dataSource).getTask(eq(taskId), mTaskCallbackCaptor.capture());
        mTaskCallbackCaptor.getValue().onDataNotAvailable();
    }

}
