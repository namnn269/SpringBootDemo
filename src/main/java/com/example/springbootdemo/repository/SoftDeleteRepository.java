package com.example.springbootdemo.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@NoRepositoryBean
public interface SoftDeleteRepository<T, ID> extends JpaRepository<T, ID> {
    @Override
    void flush();

    @Override
    <S extends T> List<S> saveAllAndFlush(Iterable<S> entities);

    @Override
    default void deleteInBatch(Iterable<T> entities) {
        JpaRepository.super.deleteInBatch(entities);
    }

    @Override
    void deleteAllInBatch(Iterable<T> entities);

    @Override
    void deleteAllByIdInBatch(Iterable<ID> ids);

    @Override
    void deleteAllInBatch();

    @Override
    T getOne(ID id);

    @Override
    T getById(ID id);

    @Override
    T getReferenceById(ID id);

    @Override
    <S extends T> List<S> findAll(Example<S> example);

    @Override
    <S extends T> List<S> findAll(Example<S> example, Sort sort);

    @Override
    <S extends T> List<S> saveAll(Iterable<S> entities);

    @Override
    List<T> findAll();

    @Override
    List<T> findAllById(Iterable<ID> ids);

    @Override
    <S extends T> S save(S entity);

    @Override
    Optional<T> findById(ID id);

    @Override
    boolean existsById(ID id);

    @Override
    long count();

    @Override
    void deleteById(ID id);

    @Override
    void delete(T entity);

    @Override
    void deleteAllById(Iterable<? extends ID> ids);

    @Override
    void deleteAll(Iterable<? extends T> entities);

    @Override
    void deleteAll();

    @Override
    List<T> findAll(Sort sort);

    @Override
    Page<T> findAll(Pageable pageable);

    @Override
    <S extends T> Optional<S> findOne(Example<S> example);

    @Override
    <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

    @Override
    <S extends T> long count(Example<S> example);

    @Override
    <S extends T> boolean exists(Example<S> example);

    @Override
    <S extends T, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction);
}
