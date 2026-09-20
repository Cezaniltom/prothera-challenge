package br.com.prothera;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Formatando para data e usando moeda no padrão Brasileiro
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat formatadorMoeda = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        formatadorMoeda.setMinimumFractionDigits(2);
        formatadorMoeda.setMaximumFractionDigits(2);

        List<Funcionario> funcionarios = new ArrayList<>(Arrays.asList(
                new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"),
                new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"),
                new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"),
                new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"),
                new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"),
                new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"),
                new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"),
                new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"),
                new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"),
                new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente")
        ));

        // Remove o funcionario João da lista
        funcionarios.removeIf(f -> f.getNome().equals("João"));

        // Retorna o slario e a data de nascimento de todos os funcionariso
        System.out.println("Funcionários");
        funcionarios.forEach(f -> imprimirFuncionario(f, formatadorData, formatadorMoeda));

        // Retorna o salario com 10% de aumento
        funcionarios.forEach(f -> {
            BigDecimal aumento = f.getSalario().multiply(new BigDecimal("0.10"));
            f.setSalario(f.getSalario().add(aumento));
        });

        // Retorna os funcionarios agrupados por função
        Map<String, List<Funcionario>> funcionariosPorFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));

        System.out.println("\nFuncionários Agrupados por Função");
        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println("\nFunção: " + funcao);
            lista.forEach(f -> System.out.println(" - " + f.getNome()));
        });

        // Retorna os funcionários que completam ano no mês 10 e 12
        System.out.println("\nAniversariantes do Mês 10 e 12");
        funcionarios.stream()
                .filter(f -> {
                    int mes = f.getDataNascimento().getMonthValue();
                    return mes == 10 || mes == 12;
                })
                .forEach(f -> System.out.println(f.getNome() + " - " + f.getDataNascimento().format(formatadorData)));

        // Reorna o funcionário mais velho
        System.out.println("\nFuncionário Mais Velho");
        Funcionario maisVelho = Collections.min(funcionarios, Comparator.comparing(Funcionario::getDataNascimento));
        long idade = ChronoUnit.YEARS.between(maisVelho.getDataNascimento(), LocalDate.now());
        System.out.println("Nome: " + maisVelho.getNome() + " | Idade: " + idade + " anos");

        // Retorna lista em ordem alfabetica
        System.out.println("\nOrdem Alfabética");
        List<Funcionario> funcionariosOrdenados = new ArrayList<>(funcionarios);
        funcionariosOrdenados.sort(Comparator.comparing(Funcionario::getNome));
        funcionariosOrdenados.forEach(f -> System.out.println(f.getNome()));

        // Retorna a soma total dos salarios
        System.out.println("\nTotal dos Salários");
        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Total: R$ " + formatadorMoeda.format(totalSalarios));

        // Retorna quantos salarios mínimos cada um ganha (Base: R$ 1212.00)
        System.out.println("\nQuantidade de Salários Mínimos");
        BigDecimal salarioMinimo = new BigDecimal("1212.00");
        funcionarios.forEach(f -> {
            BigDecimal qtdSalarios = f.getSalario().divide(salarioMinimo, 2, RoundingMode.HALF_UP);
            System.out.println(f.getNome() + " ganha " + formatadorMoeda.format(qtdSalarios) + " salários mínimos.");
        });
    }

    // Método auxiliar trazer os dados formatados
    private static void imprimirFuncionario(Funcionario f, DateTimeFormatter formatadorData, NumberFormat formatadorMoeda) {
        System.out.println(
                "Nome: " + f.getNome() +
                        " | Nascimento: " + f.getDataNascimento().format(formatadorData) +
                        " | Salário: R$ " + formatadorMoeda.format(f.getSalario()) +
                        " | Função: " + f.getFuncao()
        );
    }
}
