package com.p6.core.solution;

import com.p6.core.solution.rule.Condition;
import com.p6.core.solution.rule.Result;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Solution {
  private Map<Condition, Result> rules;
  private List<Symbol> symbols;
  private Random random;

  public Solution() {
    this.rules = new HashMap<>();
    this.symbols = new ArrayList<>();
    this.random = new Random();
  }

  public String toString() {
    return this.rules.size() + " rules, " + this.symbols.size() + " symbols";
  }

  public void createRule(Condition condition, Result result) {
    this.rules.put(condition, result);
  }

  public Set<Condition> getConditions() {
    return this.rules.keySet();
  }

  public Result getResult(Condition condition) {
    return this.rules.get(condition);
  }

  public void addSymbol(Symbol symbol) {
    this.symbols.add(symbol);
  }

  public void addAllSymbols(Collection<Symbol> symbols) {
    this.symbols.addAll(symbols);
  }

  public Integer getSymbolCount() {
    return this.symbols.size();
  }

  public Symbol chooseSymbol() {
    int symbolIndex = this.random.nextInt(this.getSymbolCount());
    Symbol symbol = this.symbols.get(symbolIndex);
    this.symbols.remove(symbolIndex);
    return symbol;
  }
}
