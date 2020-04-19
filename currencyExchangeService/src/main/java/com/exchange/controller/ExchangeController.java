package com.exchange.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.exchange.model.ConversionRates;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

@RestController
@RequestMapping(path = "/exchange")
public class ExchangeController {
	MemCachedClient mcc = null;

	@PostConstruct
	public void initializeMemCached() {
		String[] servers = { "localhost:11211" };
		SockIOPool pool = SockIOPool.getInstance("Test1");
		pool.setServers(servers);
		pool.setFailover(true);
		pool.setInitConn(10);
		pool.setMinConn(5);
		pool.setMaxConn(250);
		pool.setMaintSleep(30);
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.setAliveCheck(true);
		pool.initialize();
		// Get the Memcached Client from SockIOPool named Test1
		mcc = new MemCachedClient("Test1");
	}

	@GetMapping(path = "/syncDB", produces = "application/json")
	public String syncDBTOMemCache() {
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		Date currDate = new Date();
		String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(currDate);
		modifiedDate = modifiedDate.substring(0, modifiedDate.lastIndexOf("-") + 1);
		int tilldate = dayOfMonth - 8 >= 1 ? dayOfMonth - 8 : 1;
		String status = "Failure";
		for (int date = dayOfMonth; date >= tilldate; date--) {
			try {
				final String uri = "https://api.ratesapi.io/api/" + modifiedDate + date;
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
				headers.add("user-agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
				HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

				ResponseEntity<ConversionRates> response = restTemplate.exchange(uri, HttpMethod.GET, entity,
						ConversionRates.class);
				System.out.println(response.getBody().getDate());
				mcc.add(modifiedDate + date, response.getBody());
				status = "Success";
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return status; 
	}

	@CrossOrigin
	@GetMapping(path = "/fetchExchageData", produces = "application/json")
	public List<List<String>> fetchExchageData() {
		List<String> headerList = new ArrayList<String>();
		headerList.add("Currency");
		List<ConversionRates> conversionRatesList = new ArrayList<ConversionRates>();
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		Date currDate = new Date();
		String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(currDate);
		modifiedDate = modifiedDate.substring(0, modifiedDate.lastIndexOf("-") + 1);
		int tilldate = dayOfMonth - 8 >= 1 ? dayOfMonth - 8 : 1;
		for (int date = dayOfMonth; date >= tilldate; date--) {
			ConversionRates conversionRates = (ConversionRates) mcc.get(modifiedDate + date);
			conversionRatesList.add(conversionRates);
			headerList.add(modifiedDate + date);
		}
		List<String> gbpList = conversionRatesList.stream().map(o -> o.getRates().get("GBP"))
				.collect(Collectors.toList());
		List<String> hkdList = conversionRatesList.stream().map(o -> o.getRates().get("HKD"))
				.collect(Collectors.toList());
		List<String> idrList = conversionRatesList.stream().map(o -> o.getRates().get("IDR"))
				.collect(Collectors.toList());
		List<String> plnList = conversionRatesList.stream().map(o -> o.getRates().get("PLN"))
				.collect(Collectors.toList());
		List<String> jpyList = conversionRatesList.stream().map(o -> o.getRates().get("JPY"))
				.collect(Collectors.toList());
		List<String> inrList = conversionRatesList.stream().map(o -> o.getRates().get("INR"))
				.collect(Collectors.toList());
		List<String> chfList = conversionRatesList.stream().map(o -> o.getRates().get("CHF"))
				.collect(Collectors.toList());
		List<String> mxnList = conversionRatesList.stream().map(o -> o.getRates().get("MXN"))
				.collect(Collectors.toList());
		List<String> czkList = conversionRatesList.stream().map(o -> o.getRates().get("CZK"))
				.collect(Collectors.toList());
		List<String> sgdList = conversionRatesList.stream().map(o -> o.getRates().get("SGD"))
				.collect(Collectors.toList());
		List<String> thbList = conversionRatesList.stream().map(o -> o.getRates().get("THB"))
				.collect(Collectors.toList());
		List<String> bgnList = conversionRatesList.stream().map(o -> o.getRates().get("BGN"))
				.collect(Collectors.toList());
		List<String> usdList = conversionRatesList.stream().map(o -> o.getRates().get("USD"))
				.collect(Collectors.toList());
		List<String> nzdList = conversionRatesList.stream().map(o -> o.getRates().get("NZD"))
				.collect(Collectors.toList());
		List<String> audList = conversionRatesList.stream().map(o -> o.getRates().get("AUD"))
				.collect(Collectors.toList());

		gbpList.add(0, "GBP");
		hkdList.add(0, "HKD");
		idrList.add(0, "IDR");
		plnList.add(0, "PLN");
		jpyList.add(0, "JPY");
		inrList.add(0, "INR");
		chfList.add(0, "CHF");
		mxnList.add(0, "MXN");
		czkList.add(0, "CZK");
		sgdList.add(0, "SGD");
		thbList.add(0, "THB");
		bgnList.add(0, "BGN");
		usdList.add(0, "USD");
		nzdList.add(0, "NZD");
		audList.add(0, "AUD");

		List<List<String>> totalList = new ArrayList<List<String>>();
		totalList.add(headerList);
		totalList.add(gbpList);
		totalList.add(hkdList);
		totalList.add(idrList);
		totalList.add(plnList);
		totalList.add(jpyList);
		totalList.add(inrList);
		totalList.add(chfList);
		totalList.add(mxnList);
		totalList.add(czkList);
		totalList.add(sgdList);
		totalList.add(thbList);
		totalList.add(bgnList);
		totalList.add(usdList);
		totalList.add(nzdList);
		totalList.add(audList);

		return totalList;
	}
}
