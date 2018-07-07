package io.github.iatj.lfh2018.crawler;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainLfh2018 {

	private static final String URL_BASE = "http://busca.tjsc.jus.br/jurisprudencia/";

	public static void main(String[] args) throws IOException {
		MainLfh2018 main = new MainLfh2018();
		main.getEmentas();
	}

	public void getEmentas() throws IOException {

		Connection connect = Jsoup.connect(URL_BASE + "buscaajax.do?&categoria=acordaos");

		connect.data("datainicial", "01/01/2018");
		connect.data("datafinal", "30/06/2018");
		connect.data("busca", "avancada");
		connect.data("radio_campo", "ementa");

		Document document = connect.post();
		Elements elements = document.select("input[class=bt_integra]");

		System.out.println(elements.size());

		String pattern = "^abreIntegra\\('\\d+','([0-9A-Za-z]+).+";

		Pattern r = Pattern.compile(pattern);

		String js;
		for (Element element : elements) {
			js = element.parent().attr("onclick");
			Matcher m = r.matcher(js);

			if (m.find()) {
				getEmenta(m.group(1));
			}

		}

	}

	private void getEmenta(String ementaId) throws IOException {
		// http://busca.tjsc.jus.br/jurisprudencia/html.do?ajax=1&q=&only_ementa=&frase=&qualquer=&prox1=&prox2=&proxc=&id=AABAg7AAEAALmZOAAX&categoria=acordao_5&busca=avancada

		URLConnection connection = new URL(URL_BASE + "html.do?id=" + ementaId + "&ajax=1&categoria=acordao_5")
				.openConnection();

		String content = IOUtils.toString(connection.getInputStream(), Charset.forName("ISO-8859-1"));

		System.out.println(content);
	}
}
