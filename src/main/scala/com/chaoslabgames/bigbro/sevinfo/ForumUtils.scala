package com.chaoslabgames.bigbro.sevinfo

import java.net.URL

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Denis Rykovanov</a>
 *         on 28.08.2015.
 */
object ForumUtils {
  def calcPageHrefs(baseUrl:String, totalPosts:Int, postsPerPage:Int): Seq[String] = {
    //fixme validation, url building
    val urls = Range(0, totalPosts, postsPerPage).map( indx =>
      if (indx > 0) s"$baseUrl&start=$indx" else baseUrl
    )
    urls
  }

}
