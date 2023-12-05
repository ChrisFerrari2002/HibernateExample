package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
public class HibernateExample {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.
        createEntityManagerFactory("YourPersistenceUnit");
        try {
// Insert Data
            Note note = createAndPersistNote(entityManagerFactory, "Meeting Notes");
            Long noteId = note.getId();
            Note retrievedNote = findNoteById(entityManagerFactory, noteId);
            System.out.println("> Received note: " + retrievedNote);
        } finally {
            entityManagerFactory.close();
        }
    }
    private static Note createAndPersistNote(EntityManagerFactory factory, String content) {
        EntityManager entityManager = factory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Note note = new Note();
            note.setContent(content);
            entityManager.persist(note);
            entityManager.getTransaction().commit();
            return note;
        } catch (Exception e) {
            rollback(entityManager);
            System.err.println("Error occurred: " + e.getMessage());
            return null;
        } finally {
            closeEntityManager(entityManager);
        }
    }
    private static Note findNoteById(EntityManagerFactory factory, Long noteId) {
        EntityManager entityManager = factory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Note retrievedNote = entityManager.find(Note.class, noteId);
            entityManager.getTransaction().commit();
            return retrievedNote;
        } catch (Exception e) {
            rollback(entityManager);
            System.err.println("Error occurred: " + e.getMessage());
            return null;
        } finally {
            closeEntityManager(entityManager);
        }
    }
    private static void rollback(EntityManager entityManager) {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }
    private static void closeEntityManager(EntityManager entityManager) {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
