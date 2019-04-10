package com.socialpub.rahul.base

import com.socialpub.rahul.utils.AppConst


/**
 * The contract describes the communication between all the module elements.
 * Using a contract class ensures we are following S.O.L.I.D Principle :
 * 1. Single Responsibility Principle : A class should have one, and only one, reason to change.
 * 2. Open-closed Principle : Entities should be open for extension, but closed for modification.
 * 3. Interface Segregation Principle : A Client should not be forced to implement an interface that it doesn’t use.
 * 4. Dependency Inversion Principle : High-level modules should not depend on low-level modules. Both should depend on abstractions.
 * Abstractions should not depend on details. Details should depend on abstractions.
 */
interface BaseContract {

    /**
     * Responsible for user interactions
     */
    interface View {
        fun showLoading(message:String = AppConst.DIALOG_PLEASE_WAIT)
        fun hideLoading()
        fun onError(message: String)
    }

    /**
     * Delegates logic from the view ,
     * Avoiding creation of God Classes.
     */
    interface Controller {
        fun onStart()

    }

    /**
     * Delegates Controller from async
     * and network related task,
     * Avoiding creation of God Controller.
     */
    interface Presenter {

    }
}