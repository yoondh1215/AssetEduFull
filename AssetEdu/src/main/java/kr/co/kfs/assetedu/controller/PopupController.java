package kr.co.kfs.assetedu.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.kfs.assetedu.model.Bok01Book;
import kr.co.kfs.assetedu.model.Com01Corp;
import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Fnd01Fund;
import kr.co.kfs.assetedu.model.Itm01Item;
import kr.co.kfs.assetedu.model.Jnl10Acnt;
import kr.co.kfs.assetedu.model.Jnl11ReprAcnt;
import kr.co.kfs.assetedu.model.PageAttr;
import kr.co.kfs.assetedu.service.Bok01BookService;
import kr.co.kfs.assetedu.service.Com01CorpService;
import kr.co.kfs.assetedu.service.Fnd01FundService;
import kr.co.kfs.assetedu.service.Itm01ItemService;
import kr.co.kfs.assetedu.service.Jnl10AcntService;
import kr.co.kfs.assetedu.service.Jnl11ReprAcntService;
import kr.co.kfs.assetedu.utils.AssetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/popup")
public class PopupController {

	@Autowired
	private Com01CorpService corpService;
	
	@Autowired
	private Fnd01FundService fundService;

	@Autowired
	private Itm01ItemService itemService;

	@Autowired
	private Bok01BookService bookService;

	@Autowired
	private Jnl10AcntService jnlAcntService;

	@Autowired
	private Jnl11ReprAcntService jnlReprAcntService;
	/**
	 * 기관정보선택 팝업
	 * @param searchText
	 * @return
	 */
	@GetMapping("corp")
	public String corp(String searchText, String codeCd, String codeNm, String com01CorpType,
			@RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize,
			@RequestParam(value="currentPageNumber", required=false, defaultValue="1") Integer currentPageNumber,			
			Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("searchText : {}, codeCd : {}, codeNm: {}", searchText, codeCd, codeNm);
		log.debug("currentPageNumber : {}, pageSize : {}", currentPageNumber, pageSize);
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");

		Condition condition = new Condition();
		condition.put("searchText", searchText);
		condition.put("com01CorpType", com01CorpType);
		Long totalCount = corpService.selectCount(condition);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		//log.debug("pageAttr:{}", pageAttr);
		
		condition.putClass(pageAttr);
		
		List<Com01Corp> list;
		if(totalCount > 0) {
			list = corpService.selectList(condition);
		} else {
			Com01Corp corpModel = new Com01Corp();
			corpModel.setCom01CorpNm("조회내역이 없습니다.");
			list = new ArrayList<>();
			list.add(corpModel);
		}
		model.addAttribute("list", list);
		model.addAttribute("pageAttr", pageAttr);
		
		return "/common/popup_corp";
	}
	@GetMapping("fund")
	public String fund(String searchText, String fundCd, String fundNm, String parentYn,
			@RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize,
			@RequestParam(value="currentPageNumber", required=false, defaultValue="1") Integer currentPageNumber,			
			Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("searchText : {}, fundCd : {}, fundNm: {}, parentYn: {}", searchText, fundCd, fundNm, parentYn);
		log.debug("currentPageNumber : {}, pageSize : {}", currentPageNumber, pageSize);
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		
		Condition condition = new Condition();
		condition.put("searchText", searchText);
		condition.put("parentYn"  , parentYn);
		Long totalCount = fundService.selectCount(condition);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		log.debug("pageAttr:{}", pageAttr);
		
		condition.putClass(pageAttr);
		
		List<Fnd01Fund> list;
		if(totalCount > 0) {
			list = fundService.selectList(condition);
		} else {
			Fnd01Fund fundModel = new Fnd01Fund();
			fundModel.setFnd01FundNm("조회내역이 없습니다.");
			list = new ArrayList<>();
			list.add(fundModel);
		}
		model.addAttribute("list", list);
		model.addAttribute("pageAttr", pageAttr);
		
		return "/common/popup_fund";
	}
	@GetMapping("item")
	public String item(String searchText, String itemCd, String itemNm, 
			@RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize,
			@RequestParam(value="currentPageNumber", required=false, defaultValue="1") Integer currentPageNumber,			
			Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("searchText : {}, itemCd : {}, itemNm: {}", searchText, itemCd, itemNm);
		log.debug("currentPageNumber : {}, pageSize : {}", currentPageNumber, pageSize);
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		
		Condition condition = new Condition();
		condition.put("searchText", searchText);
		Long totalCount = itemService.selectCount(condition);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		log.debug("pageAttr:{}", pageAttr);
		
		condition.putClass(pageAttr);
		
		List<Itm01Item> list;
		if(totalCount > 0) {
			list = itemService.selectList(condition);
		} else {
			Itm01Item itemModel = new Itm01Item();
			itemModel.setItm01ItemNm("조회내역이 없습니다.");
			list = new ArrayList<>();
			list.add(itemModel);
		}
		model.addAttribute("list", list);
		model.addAttribute("pageAttr", pageAttr);
		
		return "/common/popup_item";
	}
	/**
	 * 보유종목
	 * 
	 * @param searchText : 검색어
	 * @param bookCd : 호출한 페이지의 보유원장코드가 들어갈 필드 id
	 * @param bookNm : 호출한 페이지의 보유원장명이 들어갈 필드 id
	 * @param pageSize : 페이지당 보여줄 레코드 갯수
	 * @param currentPageNumber : 보여줄 페이지
	 * @param model
	 * @return
	 */
	@GetMapping("book")
	public String book(String searchText, 
			String bookId, 
			String fundCd,
			String fundNm,
			String itemCd,
			String itemNm,
			String holdQty,
			@RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize,
			@RequestParam(value="currentPageNumber", required=false, defaultValue="1") Integer currentPageNumber,			
			Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("searchText : {}, bookId : {}, fundCd: {}, fundNm: {}, itemCd: {}, itemNm: {}, holdQty: {}", searchText, bookId ,fundCd, fundNm, itemCd, itemNm, holdQty);
		log.debug("currentPageNumber : {}, pageSize : {}", currentPageNumber, pageSize);
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		
		Condition condition = new Condition();
		condition.put("searchText", searchText);
		condition.put("holdDate", AssetUtil.ymd());
		
		Long totalCount = bookService.selectCount(condition);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		log.debug("pageAttr:{}", pageAttr);
		
		condition.putClass(pageAttr);
		
		List<Bok01Book> list = bookService.selectList(condition);
		model.addAttribute("list", list);
		model.addAttribute("pageAttr", pageAttr);
		
		return "/common/popup_book";
	}
	
	
	@GetMapping("jnl/acnt/{parentCode}")
	public String acnt(String searchText,  @PathVariable("parentCode") String parentCode,
			@RequestParam(value="acntCd", required=true ) String acntCd,
			@RequestParam(value="acntNm", required=true ) String acntNm,
			@RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize,
			@RequestParam(value="currentPageNumber", required=false, defaultValue="1") Integer currentPageNumber,			
			Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("searchText : {}, acntCd:{}, acntNm:{}", searchText, acntCd, acntNm);
		log.debug("currentPageNumber : {}, pageSize : {}", currentPageNumber, pageSize);
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		
		Condition condition = new Condition();
		condition.put("searchText", searchText);
		condition.put("parentCode", parentCode);
		
		Long totalCount = (long) jnlAcntService.selectCount(condition);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		log.debug("pageAttr:{}", pageAttr);
		
		//condition.putClass(pageAttr);
		condition.put("pageAttr", pageAttr);
		List<Jnl10Acnt> list = jnlAcntService.selectList(condition);
		model.addAttribute("list", list);
		model.addAttribute("pageAttr", pageAttr);
		
		return "/common/popup_jnl_acnt";
	}	
	@GetMapping("jnl/repr-acnt")
	public String reprAcnt(String searchText,
			@RequestParam(value="openerCdId", required=true ) String openerCdId,
			@RequestParam(value="openerNmId", required=true ) String openerNmId,
			@RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize,
			@RequestParam(value="currentPageNumber", required=false, defaultValue="1") Integer currentPageNumber,			
			Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("searchText : {}, acntCd:{}, acntNm:{}", searchText);
		log.debug("currentPageNumber : {}, pageSize : {}", currentPageNumber, pageSize);
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		
		Condition condition = new Condition();
		condition.put("searchText", searchText);
		
		
		Long totalCount = (long) jnlReprAcntService.selectCount(condition);
		PageAttr pageAttr = new PageAttr(totalCount, pageSize, currentPageNumber);
		log.debug("pageAttr:{}", pageAttr);
		
		//condition.putClass(pageAttr);
		condition.put("pageAttr", pageAttr);
		
		List<Jnl11ReprAcnt> list = jnlReprAcntService.selectList(condition);
		model.addAttribute("list", list);
		model.addAttribute("pageAttr", pageAttr);
		
		return "/common/popup_jnl_repr_acnt";
	}		
}
