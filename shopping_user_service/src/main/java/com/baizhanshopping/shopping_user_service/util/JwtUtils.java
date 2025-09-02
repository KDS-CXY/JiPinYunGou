package com.baizhanshopping.shopping_user_service.util;

import lombok.SneakyThrows;
import org.jose4j.json.JsonUtil;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
  // 私钥
  public final static String PRIVATE_JSON = "{\"kty\":\"RSA\",\"n\":\"s6WnF0ouHy4XXmd3Pfo2yAvg2lpQVLOemwCc64guKfVEKDZZF5b7wNDe6B5sjuzakiIG9BO34_2VhTCx9BjJCx-e661saN7MYHfpnt6n1PqzvA0RdJbM_EHWlr4Ic5QlwTCNOETN0BcRW9IMrQPfQb8DNUXKsRGtNPUKiUOwbzl-ZqaS4q7l9wQyxDO-f_9CXZSSHj82tbWEnhVs4iPD7r7CnoGgUPWDmnNRtWgs7QVNX8PlJfKilDD9lp9njsbRD07TbTmcuZUeGvgTk96cerjQqZCcY1VzTzXwgKOS1Yrx08HV3Xmc4LdtiGof5bdEkR3QgAxsyEl5P1e3jHkkBw\",\"e\":\"AQAB\",\"d\":\"HfriJK_bfJabjcSkkLJGHCJnVulFiK0lJeb3SRND6PqmcjOHsLkgsG_rEz-ojD2YyFRnZqo3wRObtUuIFx1fx7xyMBCUC_hKkTrRwRKVXaStBBAGPre_48U1A5kZHfi2SERK_P32fuiqm_E07la8FmPDLEvGZMB3cGQIaxnhwt3EhsoABq5vikkwS_aaUfZqmCi88tFSO4E2DXXoZX06fI2k6bvSNTp88dGTfyssZimvoWrg63k9XD3QWdYC3HGYzEmu01ubh3mjMs3PlakLU-SDVu1Xhr1fNTPL8PjS8APn-deqEgaoWW0EkhHyMx6f_qPLbMH1XxcrUvF-RjlVUQ\",\"p\":\"wz8HoFtatPiHAlzZzI3uS1FsMHkDiH2qtYAkE_dSrjzUS0F6tHxzB3iEqWosrSM0dKWgrA-sKwCBpddeSmeACSwU0NgONNmMKF7pQ3Zr4ztdZut1nE_Ok9vQn6MsrGyrvDw6N6FwKZsqXqau7LPPF26zlVeZWp9fg5JEMfiBs5k\",\"q\":\"64wGDv-s_dVYoy_hLtA56fMazY-lUG6X0mbPG0ckUeEB3LS3DwJk5QjZ9VrgOQuQP6rvRoOmxDkMvtC7EgBLIPn9ejy6hHnuQqmQ-A8zvHqmcyROlAdVlFMwG-CnPLbX9BULnGqBeLM6As1C-fa9GuryIR3iUgMrZTTu-oxeWJ8\",\"dp\":\"ww--i8uZJMJDB-5BvmZyExdmskrEZ-G84hYCdVwbY23sXr8tWxIWTflu-F7WAafxaTRsAFqsFE4uOMShNE6RERjUNw4Tq2NI7rBb9YVjJiMSHpfQ6XimFvx-Qm7gW-c58BZvx1JnX3ng6601h8_gbzWu_t4EnRAqeaVwskfnNMk\",\"dq\":\"fgF7IDzwWx3te-baLMPhp2CQnxTQF9YBy-GJu4r0SAT5jQrzvsjGFqwEhTenKX0hHp4fwHYfGWt3gQCCgp-7kRmK2wBvuvzLU8gIMFtZjuOB9aifjJrb0iwqWU91x4n8vqsfef6T4nDmgKczxCqVgTfcbZDiCIN_jHWjvk6nDfs\",\"qi\":\"RpwaMfOAmqXyJs3SlFqeUvAjJ27DTyfN_aYdnuqDyqypE44_yzA9YSgZcxoVxUktBk4xuanRi74W6sn0fTshEumIGYMLh2OCGdvmbxf-NFEgYxetNQqi56pt_3pgpfkSve7reMjGv4nEJSOYTNve8B7vOxGnzDggE7iLMkJJt0U\"}";
  // 公钥
  public final static String PUBLIC_JSON = "{\"kty\":\"RSA\",\"n\":\"s6WnF0ouHy4XXmd3Pfo2yAvg2lpQVLOemwCc64guKfVEKDZZF5b7wNDe6B5sjuzakiIG9BO34_2VhTCx9BjJCx-e661saN7MYHfpnt6n1PqzvA0RdJbM_EHWlr4Ic5QlwTCNOETN0BcRW9IMrQPfQb8DNUXKsRGtNPUKiUOwbzl-ZqaS4q7l9wQyxDO-f_9CXZSSHj82tbWEnhVs4iPD7r7CnoGgUPWDmnNRtWgs7QVNX8PlJfKilDD9lp9njsbRD07TbTmcuZUeGvgTk96cerjQqZCcY1VzTzXwgKOS1Yrx08HV3Xmc4LdtiGof5bdEkR3QgAxsyEl5P1e3jHkkBw\",\"e\":\"AQAB\"}";


  /**
   * 生成token
   *
   * @param userId    用户id
   * @param username 用户名字
   * @return
   */
  @SneakyThrows
  public static String sign(Long userId, String username) {
    // 1、 创建jwtclaims  jwt内容载荷部分
    JwtClaims claims = new JwtClaims();
    // 是谁创建了令牌并且签署了它
    claims.setIssuer("itbaizhan");
    // 令牌将被发送给谁
    claims.setAudience("audience");
    // 失效时间长 （分钟）
    claims.setExpirationTimeMinutesInTheFuture(60 * 24);
    // 令牌唯一标识符
    claims.setGeneratedJwtId();
    // 当令牌被发布或者创建现在
    claims.setIssuedAtToNow();
    // 再次之前令牌无效
    claims.setNotBeforeMinutesInThePast(2);
    // 主题
    claims.setSubject("shopping");
    // 可以添加关于这个主题得声明属性
    claims.setClaim("userId", userId);
    claims.setClaim("username", username);


    // 2、签名
    JsonWebSignature jws = new JsonWebSignature();
    //赋值载荷
    jws.setPayload(claims.toJson());


    // 3、jwt使用私钥签署
    PrivateKey privateKey = new RsaJsonWebKey(JsonUtil.parseJson(PRIVATE_JSON)).getPrivateKey();
    jws.setKey(privateKey);


    // 4、设置关键 kid
    jws.setKeyIdHeaderValue("keyId");


    // 5、设置签名算法
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
    // 6、生成jwt
    String jwt = jws.getCompactSerialization();
    return jwt;
   }




  /**
   * 解密token，获取token中的信息
   *
   * @param token
   */
  @SneakyThrows
  public static Map<String, Object> verify(String token){
    // 1、引入公钥
    PublicKey publicKey = new RsaJsonWebKey(JsonUtil.parseJson(PUBLIC_JSON)).getPublicKey();
    // 2、使用jwtcoonsumer  验证和处理jwt
    JwtConsumer jwtConsumer = new JwtConsumerBuilder()
         .setRequireExpirationTime() //过期时间
         .setAllowedClockSkewInSeconds(30) //允许在验证得时候留有一些余地 计算时钟偏差  秒
         .setRequireSubject() // 主题生命
         .setExpectedIssuer("itbaizhan") // jwt需要知道谁发布得 用来验证发布人
         .setExpectedAudience("audience") //jwt目的是谁 用来验证观众
         .setVerificationKey(publicKey) // 用公钥验证签名  验证密钥
         .setJwsAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, AlgorithmIdentifiers.RSA_USING_SHA256))
         .build();
    // 3、验证jwt 并将其处理为 claims
    try {
      JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
      return jwtClaims.getClaimsMap();
     }catch (Exception e){
      return new HashMap();
     }
   }




  public static void main(String[] args){
    // 生成
    String baizhan = sign(1001L, "baizhan");
    System.out.println(baizhan);


    Map<String, Object> stringObjectMap = verify(baizhan);
    System.out.println(stringObjectMap.get("userId"));
    System.out.println(stringObjectMap.get("username"));
   }
}
