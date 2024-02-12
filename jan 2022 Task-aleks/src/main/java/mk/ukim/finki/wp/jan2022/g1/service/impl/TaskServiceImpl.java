package mk.ukim.finki.wp.jan2022.g1.service.impl;

import mk.ukim.finki.wp.jan2022.g1.model.Task;
import mk.ukim.finki.wp.jan2022.g1.model.TaskCategory;
import mk.ukim.finki.wp.jan2022.g1.model.User;
import mk.ukim.finki.wp.jan2022.g1.model.exceptions.InvalidTaskIdException;
import mk.ukim.finki.wp.jan2022.g1.repository.TaskRepository;
import mk.ukim.finki.wp.jan2022.g1.repository.UserRepository;
import mk.ukim.finki.wp.jan2022.g1.service.TaskService;
import mk.ukim.finki.wp.jan2022.g1.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService
{

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public TaskServiceImpl(UserRepository userRepository, TaskRepository taskRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Task> listAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(InvalidTaskIdException::new);
    }

    @Override
    public Task create(String title, String description, TaskCategory category, List<Long> assignees, LocalDate dueDate) {

       List<User> assigne = userRepository.findAllById(assignees);
       Task task = new Task(title,description,category,assigne,dueDate);

        return taskRepository.save(task);
    }

    @Override
    public Task update(Long id, String title, String description, TaskCategory category, List<Long> assignees) {
        List<User> assigne = userRepository.findAllById(assignees);
        Task task = taskRepository.findById(id).orElseThrow(InvalidTaskIdException::new);
        task.setTitle(title);
        task.setAssignees(assigne);
        task.setCategory(category);
        task.setDescription(description);

        return taskRepository.save(task);
    }

    @Override
    public Task delete(Long id) {
        Task toDelete = findById(id);
        taskRepository.delete(toDelete);
        return toDelete;
    }

    @Override
    public Task markDone(Long id) {
        Task doneTask = taskRepository.findById(id).orElseThrow(InvalidTaskIdException::new);
        doneTask.setDone(true);
        return taskRepository.save(doneTask);
    }

    @Override
    public List<Task> filter(Long assigneeId, Integer lessThanDayBeforeDueDate) {
        if(assigneeId==null&&lessThanDayBeforeDueDate==null){
            return listAll();
        }
        else if(assigneeId!=null&&lessThanDayBeforeDueDate!=null)
        {
            User user=userService.findById(assigneeId);
           return taskRepository.findALLByAssigneesContainsAndDueDateBefore(user,LocalDate.now().plusDays(lessThanDayBeforeDueDate));
        }
        else if (assigneeId!=null){
            User user=userService.findById(assigneeId);
            return taskRepository.findALLByAssigneesContains(user);
        }
        else {
            return taskRepository.findAllByDueDateBefore(LocalDate.now().plusDays(lessThanDayBeforeDueDate));
        }


    }
}
