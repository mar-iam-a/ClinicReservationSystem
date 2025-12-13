package service;

import dao.DepartmentDAO;
import model.Clinic;
import model.Department;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class DepartmentService {

    private static final DepartmentDAO departmentDAO = new DepartmentDAO();

    /**
     * Retrieves the name of a department using its ID.
     */
    public static String getDepartmentNameById(int departmentId) throws SQLException {
        // نستخدم دالة getById الموجودة لديك في DepartmentDAO
        Department department = departmentDAO.getById(departmentId);
        if (department != null) {
            return department.getName();
        }
        // قيمة افتراضية في حالة عدم العثور على التخصص
        return "Unknown Specialty";
    }
    /**
     * Retrieves all departments from the database.
     * @return A list of all Department objects.
     * @throws SQLException If a database access error occurs.
     */


    public List<Department> getAllDepartments() throws SQLException {
        List<Department> allDepartments = departmentDAO.getAll();


        return allDepartments.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getAllUniqueSpecialtyNames() throws SQLException {
        List<Department> allDepartments = departmentDAO.getAll();


        return allDepartments.stream()
                .map(Department::getName)
                .distinct()
                .collect(Collectors.toList());
    }


    public int getDepartmentIdByName(String specialtyName) throws SQLException {

        return departmentDAO.getDepartmentIdByName(specialtyName);
    }

}