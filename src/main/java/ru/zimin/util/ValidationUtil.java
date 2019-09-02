package ru.zimin.util;

import ru.zimin.HasId;
import ru.zimin.util.exception.IllegalRequestDataException;
import ru.zimin.util.exception.NotFoundException;



public class ValidationUtil {

    public static <T> T checkNotFoundWithId(T object, int id, Class<T> clazz) throws NotFoundException {
        return checkNotFound(object, "type: " + clazz.getSimpleName()  + ", id=" + id);
    }

    private ValidationUtil() {
    }

    public static void checkNotFoundWithId(boolean found, int id) throws NotFoundException {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) throws NotFoundException {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) throws NotFoundException {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      http://stackoverflow.com/a/32728226/548473
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

}