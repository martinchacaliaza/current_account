package com.example.app.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.app.models.TypeCurrentAccount;

public interface TipoProductoDao extends ReactiveMongoRepository<TypeCurrentAccount, String> {

}
