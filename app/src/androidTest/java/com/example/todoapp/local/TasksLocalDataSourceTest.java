package com.example.todoapp.local;

import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.todoapp.data.Task;
import com.example.todoapp.data.source.TasksDataSource;
import com.example.todoapp.data.source.local.TasksDao;
import com.example.todoapp.data.source.local.TasksLocalDataSource;
import com.example.todoapp.data.source.local.ToDoDatabase;
import com.example.todoapp.util.AppExecutors;
import com.example.todoapp.util.SingleExecutors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import kotlin.Pair;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TasksLocalDataSourceTest {

    private final static String TITLE = "title";
    private final static String TITLE2 = "title2";
    private final static String TITLE3 = "title3";

    private TasksLocalDataSource mLocalDataSource;
    private ToDoDatabase mDatabase;

    @Before
    public void setup() {
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                ToDoDatabase.class)
                .build();
        TasksDao tasksDao = mDatabase.taskDao();
        TasksLocalDataSource.Companion.clearInstance();
        Pair<AppExecutors, TasksDao> parameters = new Pair<AppExecutors, TasksDao>(new SingleExecutors(), tasksDao);
        mLocalDataSource = TasksLocalDataSource.Companion.getInstance(parameters);
    }

    @After
    public void cleanUp() {
        mDatabase.close();
        TasksLocalDataSource.Companion.clearInstance();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mLocalDataSource);
    }

    @Test
    public void saveTask_retrievesTask() {
        final Task newTask = new Task(TITLE, "");
        mLocalDataSource.saveTask(newTask);
        mLocalDataSource.getTask(newTask.getId(), new TasksDataSource.GetTaskCallback() {

            @Override
            public void onTaskLoaded(Task task) {
                assertThat(task, is(newTask));
            }

            @Override
            public void onDataNotAvailable() {
                fail("Callback error");
            }

        });
    }

    @Test
    public void completeTaskAndRetrievedTaskIsComplete() {
        final Task newTask = new Task(TITLE, "");
        mLocalDataSource.saveTask(newTask);
        mLocalDataSource.completeTask(newTask);
        mLocalDataSource.getTask(newTask.getId(), new TasksDataSource.GetTaskCallback() {

            @Override
            public void onTaskLoaded(Task task) {
                assertThat(task, is(newTask));
                assertThat(task.isCompleted(), is(true));
            }

            @Override
            public void onDataNotAvailable() {
                fail("Callback error");
            }

        });
    }

    @Test
    public void activateTaskAndRetrievedTaskIsActive() {
        TasksDataSource.GetTaskCallback callback = mock(TasksDataSource.GetTaskCallback.class);
        final Task newTask = new Task(TITLE, "");
        mLocalDataSource.saveTask(newTask);
        mLocalDataSource.completeTask(newTask);
        mLocalDataSource.activateTask(newTask);
        mLocalDataSource.getTask(newTask.getId(), callback);
        verify(callback, never()).onDataNotAvailable();
        verify(callback).onTaskLoaded(newTask);
        assertThat(newTask.isCompleted(), is(false));
    }

    @Test
    public void clearCompletedTaskAndTaskNotRetrievable() {
        TasksDataSource.GetTaskCallback callback1 = mock(TasksDataSource.GetTaskCallback.class);
        TasksDataSource.GetTaskCallback callback2 = mock(TasksDataSource.GetTaskCallback.class);
        TasksDataSource.GetTaskCallback callback3 = mock(TasksDataSource.GetTaskCallback.class);
        final Task newTask1 = new Task(TITLE, "");
        mLocalDataSource.saveTask(newTask1);
        mLocalDataSource.completeTask(newTask1);
        final Task newTask2 = new Task(TITLE2, "");
        mLocalDataSource.saveTask(newTask2);
        mLocalDataSource.completeTask(newTask2);
        final Task newTask3 = new Task(TITLE3, "");
        mLocalDataSource.saveTask(newTask3);
        mLocalDataSource.clearCompletedTasks();
        mLocalDataSource.getTask(newTask1.getId(), callback1);
        verify(callback1).onDataNotAvailable();
        verify(callback1, never()).onTaskLoaded(newTask1);
        mLocalDataSource.getTask(newTask2.getId(), callback2);
        verify(callback2).onDataNotAvailable();
        verify(callback2, never()).onTaskLoaded(newTask1);
        mLocalDataSource.getTask(newTask3.getId(), callback3);
        verify(callback3, never()).onDataNotAvailable();
        verify(callback3).onTaskLoaded(newTask3);
    }

    @Test
    public void deleteAllTasksAndEmptyListOfRetrievedTask() {
        Task newTask = new Task(TITLE, "");
        mLocalDataSource.saveTask(newTask);
        TasksDataSource.LoadTasksCallback callback = mock(TasksDataSource.LoadTasksCallback.class);
        mLocalDataSource.deleteAllTasks();
        mLocalDataSource.getTasks(callback);
        verify(callback).onDataNotAvailable();
        verify(callback, never()).onTasksLoaded(anyListOf(Task.class));
    }

    @Test
    public void getTasksAndRetrieveSavedTasks() {
        final Task newTask1 = new Task(TITLE, "");
        mLocalDataSource.saveTask(newTask1);
        final Task newTask2 = new Task(TITLE, "");
        mLocalDataSource.saveTask(newTask2);
        mLocalDataSource.getTasks(new TasksDataSource.LoadTasksCallback() {

            @Override
            public void onTasksLoaded(@NonNull List<Task> tasks) {
                assertNotNull(tasks);
                assertTrue(tasks.size() >= 2);
                boolean newTask1IdFound = false;
                boolean newTask2IdFound = false;
                for (Task task : tasks) {
                    if (task.getId().equals(newTask1.getId())) {
                        newTask1IdFound = true;
                    }
                    if (task.getId().equals(newTask2.getId())) {
                        newTask2IdFound = true;
                    }
                }
                assertTrue(newTask1IdFound);
                assertTrue(newTask2IdFound);
            }

            @Override
            public void onDataNotAvailable() {
                fail();
            }

        });
    }

}
