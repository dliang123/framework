/*
 * Author:
 * Date:     15-1-21
 * Description: JSR 349

    已有验证注解(具体注解验证规则,请查看各注解的注释)

    @AssertFalse
    @AssertTrue
    @DecimalMax(value=, inclusive=)
    @DecimalMin(value=, inclusive=)
    @Digits(integer=, fraction=)
    @Future
    @Max(value=)
    @Min(value=)
    @NotNull
    @Null
    @Past
    @Pattern(regex=, flag=)
    @Size(min=, max=)
    @Valid

    @CreditCardNumber(ignoreNonDigitCharacters=)
    @EAN
    @Email
    @Length(min=, max=)
    @LuhnCheck(startIndex=, endIndex=, checkDigitIndex=, ignoreNonDigitCharacters=)
    @Mod10Check(multiplier=, weight=, startIndex=, endIndex=, checkDigitIndex=, ignoreNonDigitCharacters=)
    @Mod11Check(threshold=, startIndex=, endIndex=, checkDigitIndex=, ignoreNonDigitCharacters=, treatCheck10As=, treatCheck11As=)
    @NotBlank
    @NotEmpty
    @Range(min=, max=)
    @SafeHtml(whitelistType=, additionalTags=, additionalTagsWithAttributes=)
    @ScriptAssert(lang=, script=, alias=)
    @URL(protocol=, host=, port= regexp=, flags=)

 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 *            15-1-21           00000001         创建文件
 *
 */
package com.yuntu.validator;