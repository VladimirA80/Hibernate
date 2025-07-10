package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


import java.util.Collections;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            String sql = """
                    CREATE TABLE IF NOT EXISTS users (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        last_name VARCHAR(50) NOT NULL,
                        age TINYINT
                    )
                    """;

            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("Таблица users создана");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Ошибка создания таблицы: " + e.getMessage());

        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            String sql = "DROP TABLE IF EXISTS users";

            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("Таблица users удалена");

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Ошибка удаления таблицы: " + e.getMessage());
        }
    }


    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try {Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            User user = new User(name, lastName, age);
            session.save(user);

            transaction.commit();
            System.out.println("Пользователь " + name + " " + lastName + " успешно сохранен");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Ошибка при сохранении пользователя: " + e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
         Transaction transaction = null;
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            User user = session.get(User.class, id);

            if (user != null) {
                session.delete(user);
                transaction.commit();
                System.out.printf("Пользователь с ID %d удален (%s %s)%n",
                        id, user.getName(), user.getLastName());
            } else {
                transaction.rollback();
                System.out.printf("Пользователь с ID %d не найден%n", id);
            }

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.printf("Ошибка при удалении пользователя с ID %d: %s%n",
                    id, e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            Session session = sessionFactory.openSession();

            String hql = "FROM User";

            List<User> users = session.createQuery(hql, User.class).getResultList();

            System.out.println("Получено " + users.size() + " пользователей");
            return users;

        } catch (Exception e) {
            System.err.println("Ошибка при получении пользователей: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            String deleteSql = "DELETE FROM users";
            session.createNativeQuery(deleteSql).executeUpdate();

            transaction.commit();
            System.out.println("Таблица users очищена");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Ошибка очистки таблицы: " + e.getMessage());
        }
    }
}
