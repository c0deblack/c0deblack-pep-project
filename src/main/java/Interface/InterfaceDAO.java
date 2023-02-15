package Interface;

import Errors.AddError;
import Errors.Message.MessageDeleteError;
import Errors.ReadError;
import Errors.UpdateError;
import Model.Account;

import java.util.List;

/**
 * Provides a simple CRUD interface.
 * @param <T> Model used to implement the interface.
 */
public interface InterfaceDAO<T> {
    public T add(T t) throws AddError;
    public T get(int id) throws ReadError;
    public List<T> getAll() throws ReadError;
    public T update(T t) throws UpdateError;
    public T delete(T t) throws MessageDeleteError;
}
