package com.sms.student_management.Config;

import com.sms.student_management.Entity.*;
import com.sms.student_management.Repository.*;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DemoDataSeeder {
    public static final String DEMO_PASSWORD = "Demo123!";

    @Bean
    CommandLineRunner seedDemoData(
            UserRepository users,
            StudentRepository students,
            TeacherRepository teachers,
            ParentRepository parents,
            CourseRepository courses,
            ClassSectionRepository sections,
            EnrollmentRepository enrollments,
            AssessmentRepository assessments,
            GradeRepository grades,
            AttendanceSessionRepository sessions,
            AttendanceRecordRepository records,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (users.count() > 0) return;

            User adminUser = users.save(user("Alex Morgan", "admin@demo.edu", User.Role.ADMIN, passwordEncoder));
            User teacherUser = users.save(user("Dr. Maya Patel", "teacher@demo.edu", User.Role.TEACHER, passwordEncoder));
            User studentUser = users.save(user("Jordan Lee", "student@demo.edu", User.Role.STUDENT, passwordEncoder));
            User parentUser = users.save(user("Taylor Lee", "parent@demo.edu", User.Role.PARENT, passwordEncoder));

            Teacher teacher = new Teacher();
            teacher.setUser(teacherUser);
            teacher.setEmployeeNumber("T-1042");
            teacher.setSpecialization("Computer Science");
            teacher = teachers.save(teacher);

            Parent parent = new Parent();
            parent.setUser(parentUser);
            parents.save(parent);

            Student jordan = student("Jordan", "Lee", "student@demo.edu", LocalDate.of(2006, 4, 12));
            Student priya = student("Priya", "Shah", "priya.shah@demo.edu", LocalDate.of(2006, 8, 25));
            Student ethan = student("Ethan", "Williams", "ethan.williams@demo.edu", LocalDate.of(2005, 11, 3));
            Student sofia = student("Sofia", "Martinez", "sofia.martinez@demo.edu", LocalDate.of(2006, 2, 18));
            students.saveAll(List.of(jordan, priya, ethan, sofia));

            Course algorithms = courses.save(course("Data Structures & Algorithms", "CS-301"));
            Course databases = courses.save(course("Database Systems", "CS-315"));
            Course cloud = courses.save(course("Cloud Application Development", "CS-340"));

            ClassSection sectionA = sections.save(section(algorithms, teacher, "CS-301 · Section A"));
            ClassSection sectionB = sections.save(section(databases, teacher, "CS-315 · Section B"));
            ClassSection sectionC = sections.save(section(cloud, teacher, "CS-340 · Section A"));

            enrollments.saveAll(List.of(
                    enrollment(jordan, sectionA), enrollment(jordan, sectionB), enrollment(jordan, sectionC),
                    enrollment(priya, sectionA), enrollment(priya, sectionB),
                    enrollment(ethan, sectionA), enrollment(ethan, sectionC),
                    enrollment(sofia, sectionB), enrollment(sofia, sectionC)));

            Assessment midterm = assessments.save(assessment("Algorithms Midterm", 100, sectionA));
            Assessment schemaLab = assessments.save(assessment("Schema Design Lab", 100, sectionB));
            Assessment cloudProject = assessments.save(assessment("Cloud Deployment Project", 100, sectionC));

            grades.saveAll(List.of(
                    grade(jordan, midterm, 92), grade(jordan, schemaLab, 88), grade(jordan, cloudProject, 95),
                    grade(priya, midterm, 96), grade(priya, schemaLab, 91),
                    grade(ethan, midterm, 84), grade(ethan, cloudProject, 89),
                    grade(sofia, schemaLab, 94), grade(sofia, cloudProject, 97)));

            AttendanceSession firstSession = sessions.save(session(sectionA, LocalDate.now().minusDays(2)));
            AttendanceSession secondSession = sessions.save(session(sectionB, LocalDate.now().minusDays(1)));
            records.saveAll(List.of(
                    attendance(jordan, firstSession, true), attendance(priya, firstSession, true),
                    attendance(ethan, firstSession, false), attendance(sofia, firstSession, true),
                    attendance(jordan, secondSession, true), attendance(priya, secondSession, true),
                    attendance(ethan, secondSession, true), attendance(sofia, secondSession, true)));
        };
    }

    private static User user(String name, String email, User.Role role, PasswordEncoder encoder) {
        User user = new User();
        user.setDisplayName(name);
        user.setEmail(email);
        user.setRole(role);
        user.setPasswordHash(encoder.encode(DEMO_PASSWORD));
        return user;
    }

    private static Student student(String first, String last, String email, LocalDate dateOfBirth) {
        Student student = new Student();
        student.setFirstName(first);
        student.setLastName(last);
        student.setEmail(email);
        student.setDateOfBirth(dateOfBirth);
        return student;
    }

    private static Course course(String name, String code) {
        Course course = new Course();
        course.setName(name);
        course.setCode(code);
        return course;
    }

    private static ClassSection section(Course course, Teacher teacher, String name) {
        ClassSection section = new ClassSection();
        section.setCourse(course);
        section.setTeacher(teacher);
        section.setSectionName(name);
        return section;
    }

    private static Enrollment enrollment(Student student, ClassSection section) {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setClassSection(section);
        enrollment.setStatus("ACTIVE");
        return enrollment;
    }

    private static Assessment assessment(String title, int maxMarks, ClassSection section) {
        Assessment assessment = new Assessment();
        assessment.setTitle(title);
        assessment.setMaxMarks(maxMarks);
        assessment.setClassSection(section);
        return assessment;
    }

    private static Grade grade(Student student, Assessment assessment, int marks) {
        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setAssessment(assessment);
        grade.setMarks(marks);
        return grade;
    }

    private static AttendanceSession session(ClassSection section, LocalDate date) {
        AttendanceSession session = new AttendanceSession();
        session.setClassSection(section);
        session.setSessionDate(date);
        return session;
    }

    private static AttendanceRecord attendance(Student student, AttendanceSession session, boolean present) {
        AttendanceRecord record = new AttendanceRecord();
        record.setStudent(student);
        record.setSession(session);
        record.setPresent(present);
        return record;
    }
}
