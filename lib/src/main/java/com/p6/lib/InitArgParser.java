package com.p6.lib;

@FunctionalInterface
public interface InitArgParser<T> {
  T parseArgs(Object[] args);
}
