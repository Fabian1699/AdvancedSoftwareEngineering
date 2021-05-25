package com.example.taskforce.adapters.database;

import com.example.taskforce.application.ITaskDAO;
import com.example.taskforce.application.TaskFactory;
import com.example.taskforce.domain.task.Frequency;
import com.example.taskforce.domain.task.SubTask;
import com.example.taskforce.domain.task.Task;
import com.example.taskforce.domain.task.TaskBase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskDAO implements ITaskDAO {
    private final IDatabaseHelper dbHelper;

    public TaskDAO(IDatabaseHelper databaseHelper){
        this.dbHelper =databaseHelper;
    }

    public List<Task> getAllTaskObjects(){
        List<Task> taskObjects = new ArrayList<>();
        List<Map<TaskObjectValues, String>> taskValues = dbHelper.getTasks();

        return taskValues.stream()
                .map(taskWithStringValues -> generateTaskFromStringValues(taskWithStringValues))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public Optional<Task> getTask(UUID id){
        return generateTaskFromStringValues(dbHelper.getTask(id.toString()));
    }

    private Optional<Task> generateTaskFromStringValues(Map<TaskObjectValues, String> taskObjectWithStringValues) {
        TaskFactory fac = new TaskFactory();

        UUID objId = UUID.fromString(taskObjectWithStringValues.get(TaskObjectValues.ID));
        fac.setId(objId);
        fac.setTaskName(taskObjectWithStringValues.get(TaskObjectValues.NAME));

        try {
            fac.setTargetDate(new SimpleDateFormat("yyyy-MM-dd")
                    .parse(taskObjectWithStringValues.get(TaskObjectValues.TARGET_DATE)));
            fac.setFinishDate(new SimpleDateFormat("yyyy-MM-dd")
                    .parse(taskObjectWithStringValues.get(TaskObjectValues.FINISH_DATE)));
            fac.setFrequency(Frequency.fromKey(taskObjectWithStringValues.get(TaskObjectValues.FREQUENCY)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        fac.setTimeSpentMinutes(Integer.valueOf(taskObjectWithStringValues.get(TaskObjectValues.TIME_SPENT)));
        fac.setFinished(Boolean.valueOf(taskObjectWithStringValues.get(TaskObjectValues.FINISHED)));

        List<Map<TaskObjectValues, String>> subTasksWithStringValues = dbHelper.getSubTasks(objId.toString());
        Set<SubTask> subTasks = subTasksWithStringValues.stream()//
                .map(sub -> generateSubTaskFromStringValues(sub))//
                .collect(Collectors.toSet());

        fac.setSubTasks(subTasks);
        return fac.build();
    }

    private SubTask generateSubTaskFromStringValues(Map<TaskObjectValues, String> subTaskValues){
        String subTaskName = subTaskValues.get(TaskObjectValues.NAME);
        boolean isSubTaskFinished = Boolean.valueOf(subTaskValues.get(TaskObjectValues.FINISHED));
        return new SubTask(subTaskName, isSubTaskFinished);
    }

    public boolean saveTaskToDatabase(Task task){
        TaskBase taskBase = task.getTaskObjectCopy().getTaskBase();

        boolean worked = dbHelper.addTask(
                task.getId().toString(),
                taskBase.getName(),
                new SimpleDateFormat("yyyy-MM-dd").format(taskBase.getTargetDate()),
                new SimpleDateFormat("yyyy-MM-dd").format(taskBase.getTargetDate()),
              //  new SimpleDateFormat("yyyy-MM-dd").format(task.getFinishDate()),
              //  String.valueOf(task.getTimeSpentMinutes()),
                String.valueOf(0),
                taskBase.getFrequency().getKey(),
                String.valueOf(task.isFinished()));

        for(SubTask sub: task.getSubTasks()){
            worked &= dbHelper.addSubTask(task.getId().toString(), sub.getTaskName(), String.valueOf(sub.isFinished()));
        }
        return worked;
    }

    public void updateTask(Task task){
        dbHelper.deleteTask(task.getId().toString());
        saveTaskToDatabase(task);
/*        dbHelper.updateTaskFinished(
                taskObject.getId().toString(),
                new SimpleDateFormat("yyyy-MM-dd").format(taskObject.getFinishDate()),
                String.valueOf(taskObject.getTimeSpentMinutes()),
                String.valueOf(taskObject.isFinished()));

 */
    }

    public void updateSubTask(UUID taskId, SubTask subTask){
        dbHelper.updateSubTaskFinished(taskId.toString(), subTask.getTaskName(), subTask.isFinished());
    }

    public void deleteTaskFromDatabase(UUID id){
        dbHelper.deleteTask(id.toString());
    }
}
