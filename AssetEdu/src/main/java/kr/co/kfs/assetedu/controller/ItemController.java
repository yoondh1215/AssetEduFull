package kr.co.kfs.assetedu.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.kfs.assetedu.model.ApiData;
import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Itm01Item;
import kr.co.kfs.assetedu.model.Itm02EvalPrice;
import kr.co.kfs.assetedu.service.Com02CodeService;
import kr.co.kfs.assetedu.service.Itm01ItemService;
import kr.co.kfs.assetedu.service.Itm02EvalPriceService;
import kr.co.kfs.assetedu.utils.AssetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 종목정보 Controller
 * 
 *
 */
@Controller
@Slf4j
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private Itm01ItemService service;

	@Autowired
	private Itm02EvalPriceService priceService;

	@Autowired
	private Com02CodeService codeService;

	@GetMapping("list")
	public String list(String searchText,  Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("종목리스트");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		model.addAttribute("pageTitle","종목정보-리스트");
		Condition condition = new Condition();
		condition.put("searchText", searchText);
		List<Itm01Item> list = service.selectList(condition);
		model.addAttribute("list", list);
		return "/item/list";
	}

	@GetMapping("insert")
	public String insert(Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("신규종목 등록 화면");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");

		model.addAttribute("pageTitle","종목등록");
		model.addAttribute("item",new Itm01Item());
		model.addAttribute("stkListTypeList", codeService.codeList("ListType"));
		model.addAttribute("marketTypeList" , codeService.codeList("MarketType"));
		model.addAttribute("stkTypeList"    , codeService.codeList("StkType"));

		return "/item/insert_form";
	}

	@PostMapping("insert")
	public String insert(@Valid @ModelAttribute("item") Itm01Item item, BindingResult bindingResult, RedirectAttributes redirectAttr, Model model) throws UnsupportedEncodingException {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("종목정보를 테이블에 저장하고 리스트로 이동");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("item: {}", item);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("stkListTypeList", codeService.codeList("ListType"));
			model.addAttribute("marketTypeList" , codeService.codeList("MarketType"));
			model.addAttribute("stkTypeList"    , codeService.codeList("StkType"));
			return "/item/insert_form";	
		}

		String msg;
		
		//종목코드 중복체크
		Itm01Item checkItem = service.selectOne(item);
		if(checkItem != null) {
			msg = String.format("\"%s\" 코드는 이미 \"%s\" 종목으로 등록되어있습니다.", checkItem.getItm01ItemCd(), checkItem.getItm01ItemNm());
			model.addAttribute("stkListTypeList", codeService.codeList("ListType"));
			model.addAttribute("marketTypeList" , codeService.codeList("MarketType"));
			model.addAttribute("stkTypeList"    , codeService.codeList("StkType"));
			bindingResult.addError(new FieldError("", "", msg));
			return "/item/insert_form";
		} else {
			int affectedCount = service.insert(item);
			log.debug("DB에 적용된 갯수 : {}",affectedCount);

			msg = String.format("\"%s\" 주식종목이 등록되었습니다", item.getItm01ItemNm());
			redirectAttr.addAttribute("mode", "insert");
			redirectAttr.addAttribute("msg" , msg);
			return "redirect:/item/success";
		}
	}

	@GetMapping("update")
	public String update(@ModelAttribute("item") Itm01Item item, Model model) throws UnsupportedEncodingException {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("종목정보 상세보기 및 수정 form 표시");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		item = service.selectOne(item);

		model.addAttribute("item", item);
		model.addAttribute("stkListTypeList", codeService.codeList("ListType"));
		model.addAttribute("marketTypeList" , codeService.codeList("MarketType"));
		model.addAttribute("stkTypeList"    , codeService.codeList("StkType"));

		return "/item/update_form";
	}

	/**
	 * 종목정보 수정 데이터입력
	 * @param item
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@PostMapping("update")
	public String update_form(@ModelAttribute("item") Itm01Item item, Model model) throws UnsupportedEncodingException {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("종목정보 수정");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("item: {}", item);

		service.update(item);
		String msg = String.format("\"%s\" 종목이 수정되었습니다.", item.getItm01ItemNm());
		return "redirect:/item/success?mode=update&itemCd=" + item.getItm01ItemCd()+"&msg=" + URLEncoder.encode(msg,"UTF-8");
	}

	@GetMapping("/delete")
	public String delete(@ModelAttribute Itm01Item item) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("종목정보삭제 삭제할 종목코드 :" + item.getItm01ItemCd());
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		
		int deletedCount = service.delete(item);
		if(deletedCount > 0) {
			log.warn("종목코드 : {}가 삭제되었습니다");
		}
		return "redirect:/item/list";
	}

	@GetMapping("success")
	public String success(String msg, String mode, String itemCd, Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("펀드등록 화면");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");

		model.addAttribute("pageTitle","종목등록");
		model.addAttribute("msg", msg);
		model.addAttribute("mode", mode);
		model.addAttribute("itemCd", itemCd);

		return "/item/success";
	}

	/**
	 * 평가단가 조회
	 */
	@GetMapping("price")
	public String price(String searchText, String stdDate,  Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("보유종목 평가단가 리스트");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		model.addAttribute("pageTitle","평가단가 리스트");
		
		if(Objects.isNull(stdDate)) {
			stdDate = AssetUtil.today();
		} else {
			stdDate = AssetUtil.removeDash(stdDate);
		}
		
		Condition condition = new Condition();
		condition.put("searchText", searchText);
		condition.put("stdDate"   , stdDate);
		
		List<Itm02EvalPrice> list = priceService.selectList(condition);
		
		model.addAttribute("list"   , list);
		model.addAttribute("stdDate", AssetUtil.displayYmd(stdDate));
		
		return "/item/price";
	}
	
	/**
	 * 평가단가 입력&변경 (ajax로 modal에서 호출)
	 * 
	 * @param data
	 * @return
	 */
	@ResponseBody
	@PostMapping("price_update")
	public String price_update(@Valid @RequestBody Itm02EvalPrice itm02EvalPrice) {

		log.debug("=========> 안오지????");

		itm02EvalPrice.setItm02ApplyDate (AssetUtil.removeDash (itm02EvalPrice.getItm02ApplyDate()));
		itm02EvalPrice.setItm02ApplyPrice(AssetUtil.removeComma(itm02EvalPrice.getItm02ApplyPrice()+""));
		
		log.debug("=========> itm02EvalPrice : {}", itm02EvalPrice);

		ApiData apiData = new ApiData();
		try {
			int count = priceService.update(itm02EvalPrice);
			
			apiData.put("count", count);
			apiData.put("result", "OK");
			apiData.put("itm02EvalPrice", itm02EvalPrice);
			apiData.put("msg", "저장이 완료되었습니다. (수정된 레코드갯수 : "+count+")" );
			
		} catch (Exception e) {
			apiData.put("result", "NK:" + e.getMessage());
		}
		return apiData.toJson();
	}

}
