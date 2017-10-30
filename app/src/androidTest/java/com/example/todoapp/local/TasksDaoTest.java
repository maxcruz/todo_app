package com.example.todoapp.local;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.todoapp.data.Task;
import com.example.todoapp.data.source.local.ToDoDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TasksDaoTest {

    private static final Task TASK = new Task("id", "title", "description", true);

    private ToDoDatabase mDatabase;

    @Before
    public void initDb() {
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                ToDoDatabase.class).build();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }

    @Test
    public void insertTaskAndGetById() {
        mDatabase.taskDao().insertTask(TASK);
        Task loaded = mDatabase.taskDao().getTaskById(TASK.getId());
        assertTask(loaded, "id", "title", "description", true);
    }

    @Test
    public void insertTaskAndGetTasks() {
        mDatabase.taskDao().insertTask(TASK);
        List<Task> tasks = mDatabase.taskDao().getTasks();
        assertThat(tasks.size(), is(1));
        assertTask(tasks.get(0), "id", "title", "description", true);
    }

    @Test
    public void updateTaskAndGetById() {
        mDatabase.taskDao().insertTask(TASK);
        Task updatedTask = new Task("id", "title2", "description2", true);
        mDatabase.taskDao().updateTask(updatedTask);
        Task loaded = mDatabase.taskDao().getTaskById("id");
        assertTask(loaded, "id", "title2", "description2", true);
    }

    @Test
    public void updateCompletedAndGetById() {
        mDatabase.taskDao().insertTask(TASK);
        mDatabase.taskDao().updateCompleted(TASK.getId(), false);
        Task loaded = mDatabase.taskDao().getTaskById("id");
        assertTask(loaded, TASK.getId(), TASK.getTitle(), TASK.getDescription(), false);
    }

    @Test
    public void deleteTaskByIdAndGettingTasks() {
        mDatabase.taskDao().insertTask(TASK);
        mDatabase.taskDao().deleteTaskById(TASK.getId());
        List<Task> tasks = mDatabase.taskDao().getTasks();
        assertThat(tasks.size(), is(0));
    }

    @Test
    public void deleteTasksAndGettingTasks() {
        mDatabase.taskDao().insertTask(TASK);
        mDatabase.taskDao().deleteTasks();
        List<Task> tasks = mDatabase.taskDao().getTasks();
        assertThat(tasks.size(), is(0));
    }

    @Test
    public void deleteCompletedTasksAndGettingTasks() {
        mDatabase.taskDao().insertTask(TASK);
        mDatabase.taskDao().deleteCompletedTasks();
        List<Task> tasks = mDatabase.taskDao().getTasks();
        assertThat(tasks.size(), is(0));
    }

    private void assertTask(Task task, String id, String title,
            String description, boolean completed) {
        assertThat(task, notNullValue());
        assertThat(task.getId(), is(id));
        assertThat(task.getTitle(), is(title));
        assertThat(task.getDescription(), is(description));
        assertThat(task.isCompleted(), is(completed));
    }

}
