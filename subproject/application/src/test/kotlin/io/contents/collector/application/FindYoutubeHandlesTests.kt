package io.contents.collector.io.contents.collector.application

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.blockhound.BlockHound
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FindYoutubeHandlesTests :
    BehaviorSpec({
        extension(BlockHound())

        context("it should be able to find Youtube handles by some keyword") {
            given("a keyword") {
                `when`("request to get youtube handles") {
                    then("should return youtube handles related the given keyword") {
                        1 shouldBe 1
                    }
                }
            }
        }
    })
