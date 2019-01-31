package com.p6.core.solution;

import com.p6.core.solution.rule.Condition;
import com.p6.core.solution.rule.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Solution {
  private Map<Condition, Result> rules;
  private List<Symbol> symbols;

  public Solution() {
    this.rules = new HashMap<>();
    this.symbols = new ArrayList<>();
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

  public Integer getSymbolCount() {
    return this.symbols.size();
  }

  public Symbol getSymbol(Integer id) {
    return this.symbols.get(id);
  }

  public List<Symbol> getSymbols() {
    return this.symbols;
  }

  public void removeSymbol(int id) {
    this.symbols.remove(id);
  }
}
