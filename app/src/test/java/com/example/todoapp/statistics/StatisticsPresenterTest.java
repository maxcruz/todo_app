package com.example.todoapp.statistics;

import com.example.todoapp.data.Task;
import com.example.todoapp.data.source.TasksDataSource;
import com.example.todoapp.data.source.TasksRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StatisticsPresenterTest {

    private static List<Task> TASKS;

    @Mock
    private TasksRepository mTasksRepository;

    @Mock
    private StatisticsContract.View mStatisticsView;

    @Captor
    private ArgumentCaptor<TasksDataSource.LoadTasksCallback> mLoadTasksCallbackCaptor;

    private StatisticsPresenter mStatisticsPresenter;

    @Before
    public void setupStatisticsPresenter() {
        MockitoAnnotations.initMocks(this);
        mStatisticsPresenter = new StatisticsPresenter(mTasksRepository, mStatisticsView);
        when(mStatisticsView.isActive()).thenReturn(true);
        TASKS = new ArrayList<Task>() {{
            new Task("Title1", "Description1");
            new Task("Title2", "Description2", true);
            new Task("Title3", "Description3", true);
        }};
    }

    @Test
    public void createPresenterAndSetsThePresenterToView() {
        mStatisticsPresenter = new StatisticsPresenter(mTasksRepository, mStatisticsView);
        verify(mStatisticsView).setPresenter(mStatisticsPresenter);
    }

    @Test
    public void loadEmptyTasksFromRepositoryAndCallViewToDisplay() {
        TASKS.clear();
        mStatisticsPresenter.start();
        verify(mStatisticsView).setProgressIndicator(true);
        verify(mTasksRepository).getTasks(mLoadTasksCallbackCaptor.capture());
        mLoadTasksCallbackCaptor.getValue().onTasksLoaded(TASKS);
        verify(mStatisticsView).setProgressIndicator(false);
        verify(mStatisticsView).showStatistics(0, 0);
    }

    @Test
    public void loadStatisticsWhenTasksAreUnavailableAndCallErrorToDisplay() {
        mStatisticsPresenter.start();
        verify(mTasksRepository).getTasks(mLoadTasksCallbackCaptor.capture());
        mLoadTasksCallbackCaptor.getValue().onDataNotAvailable();
        verify(mStatisticsView).showLoadingStatisticsError();
    }

}
