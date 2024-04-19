package ru.practicum.shareit.base;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


public abstract class BaseController<T, ID> {

    @PostMapping
    public ResponseEntity<T> create(@Valid @RequestBody T entity,
                                    @RequestHeader(name = "X-Sharer-User-Id", required = false) ID id) {
        T createdEntity = createEntity(entity, id);
        return ResponseEntity.ok(createdEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> read(@PathVariable ID id) {
        T entity = getEntityById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> update(@Validated(Update.class) @RequestBody T entity,
                                    @RequestHeader(name = "X-Sharer-User-Id", required = false) ID id) {
        T updatedEntity = updateEntity(entity, id);
        return updatedEntity != null ? ResponseEntity.ok(updatedEntity) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        boolean deleted = deleteEntity(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<T>> list() {
        List<T> entities = getAllEntities();
        return ResponseEntity.ok(entities);
    }

    protected abstract T createEntity(T entity, ID id);

    protected abstract T getEntityById(ID id);

    protected abstract T updateEntity(T entity, ID id);

    protected abstract boolean deleteEntity(ID id);

    protected abstract List<T> getAllEntities();
}
