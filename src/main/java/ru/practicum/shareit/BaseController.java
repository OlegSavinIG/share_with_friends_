package ru.practicum.shareit;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.annotation.Update;

import javax.validation.Valid;
import java.util.List;


public abstract class BaseController<T, D> {

    @PostMapping
    public ResponseEntity<T> create(@Valid @RequestBody T entity,
                                    @RequestHeader(name = "X-Sharer-User-Id", required = false) D userId) {
        T createdEntity = createEntity(entity, userId);
        return ResponseEntity.ok(createdEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable D id,
                                     @RequestHeader(name = "X-Sharer-User-Id", required = false) D userId) {
        T entity = getEntityById(id, userId);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<T> update(@Validated(Update.class) @RequestBody T entity,
                                    @PathVariable D id,
                                    @RequestHeader(name = "X-Sharer-User-Id", required = false) D userId) {
        T updatedEntity = updateEntity(entity, id, userId);
        return updatedEntity != null ? ResponseEntity.ok(updatedEntity) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable D id) {
        deleteEntity(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<T>> getAll(@RequestHeader(name = "X-Sharer-User-Id", required = false) D userId,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        List<T> entities = getAllEntities(userId, from, size);
        return ResponseEntity.ok(entities);
    }

    protected abstract T createEntity(T entity, D userId);

    protected abstract T getEntityById(D id, D userId);

    protected abstract T updateEntity(T entity, D id, D userId);

    protected abstract void deleteEntity(D id);

    protected abstract List<T> getAllEntities(D userId, int from, int size);
}
