package com.example.exceptions

class InsertFailedException(message: String) : Exception(message)

class ReadFailedException(message: String) : Exception(message)

class UpdateFailedException(message: String) : Exception(message)

class DeleteFailedException(message: String) : Exception(message)

class UserExistsException(message: String) : Exception(message)