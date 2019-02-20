package com.mylnikov

import java.util.Date

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import scala.beans.BeanProperty

@JsonIgnoreProperties(ignoreUnknown = true)
case class BookingEvent(@BeanProperty var dateTime: Date = null,
                        @BeanProperty var siteName: Int = 0,
                        @BeanProperty var posaContinent: Int = 0,
                        @BeanProperty var userLocationCountry: Int = 0,
                        @BeanProperty var userLocationRegion	: Int = 0,
                        @BeanProperty var userLocationCity	: Int = 0,
                        @BeanProperty var origDestinationDistance	: Double = 0,
                        @BeanProperty var userId	: Int = 0,
                        @BeanProperty var mobile	: Boolean = false,
                        @BeanProperty var package_	: Boolean = false,
                        @BeanProperty var channel	: Int = 0,
                        @BeanProperty var srchCi	: String = null,
                        @BeanProperty var srchCo	: String = null,
                        @BeanProperty var srchAdultsCnt	: Int = 0,
                        @BeanProperty var srchRmCnt	: Int = 0,
                        @BeanProperty var srchDestinationId	: Int = 0,
                        @BeanProperty var srchDestinationTypeId	: Int = 0,
                        @BeanProperty var hotelContinent	: Int = 0,
                        @BeanProperty var hotelMarket	: Int = 0,
                        @BeanProperty var booking	: Boolean = false,
                        @BeanProperty var cnt	: Long = 0,
                        @BeanProperty var hotelCluster	: Int = 0
                       ) {

  def this() {
    this(dateTime = new Date(), srchCi = "", srchCo = "")
  }

}

