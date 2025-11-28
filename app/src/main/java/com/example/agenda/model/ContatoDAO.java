package com.example.agenda.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContatoDAO {
    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper; // Classe Helper

    // Construtor - instancia o Helper

    public ContatoDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    // Abre o database para escrita
    public void open() {
        database = databaseHelper.getWritableDatabase();
    }

    // Fecha o database
    public void close() {
        databaseHelper.close();
    }

    // Métodos CRUD

    public long adicionarContato(Contato contato){

        // Cria um objeto values para os valores a serem inseridos

        ContentValues values = new ContentValues();
        values.put("nome", contato.getNome());
        values.put("email", contato.getEmail());
        values.put("telefone", contato.getTelefone());
        values.put("foto", contato.getFoto());

        // Metodo para inserir - Parâmetros: nome da tabela "contato" e os valores
        return database.insert("contato",null, values);
    }

    // Metodo para SELECT - retornando uma lista dos contatos buscados

    public List<Contato> listarContatos(){

        List<Contato> contatos = new ArrayList<>();

        // Cursor para percorrer os resultados - query na tabela "contato"

        Cursor cursor = database.query("contato", null, null, null, null, null, "nome ASC");

        // Cursor no inicio

        if (cursor.moveToFirst()){

            do{// Repete

                Contato contato = new Contato();// Cria uma instancia de Contato com os valores

                contato.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));

                contato.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

                contato.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));

                contato.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));

                contato.setFoto(cursor.getString(cursor.getColumnIndexOrThrow("foto")));

                contatos.add(contato); // Adiciona o contato à lista

            }while(cursor.moveToNext());// Cursor para o proximo

        }

        cursor.close();

        return contatos; // Retorna a lista

    }

    // Metodo para fazer o update de um contato

    public int atualizarContato(Contato contato){

        ContentValues values = new ContentValues();

        values.put("nome", contato.getNome());

        values.put("email", contato.getEmail());

        values.put("telefone", contato.getTelefone());

        values.put("foto", contato.getFoto());

        //update na tabela "contato" com os valores

        return database.update("contato", values, "id=?", new String[]{String.valueOf(contato.getId())});

    }

    // Metodo para delete pelo id

    public void apagarContato(int id){
        //delete na tabela "contato"
        database.delete("contato", "id=?", new String[]{String.valueOf(id)});

    }

    // Metodo para SELECT por um id - retorna um contato

    public Contato buscaContatoPorId(int id){

        Cursor cursor = database.query("contato", null, "id=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()){

            Contato contato = new Contato();

            contato.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));

            contato.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

            contato.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));

            contato.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));

            contato.setFoto(cursor.getString(cursor.getColumnIndexOrThrow("foto")));

            cursor.close();

            return contato;

        }
        return null;
    }
}

// Fluxo de Dados entre as camada Model e Controller

//Controller → Solicita operação ao DAO

//DAO → Executa SQL através do DatabaseHelper

//DatabaseHelper → Gerencia conexão e execução

//SQLite → Processa comando e retorna resultado

//DAO → Converte resultado em objetos Java

//Controller → Recebe objetos processados