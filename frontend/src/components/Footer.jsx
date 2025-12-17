import React from "react";
import { Facebook, Instagram, X } from "lucide-react";

export default function Footer() {
  return (
    <footer className="bg-white border-t border-gray-200 mt-24">
      <div className="max-w-7xl mx-auto px-6 py-10 flex flex-col md:flex-row items-center justify-between gap-6">
        
        {/* Left */}
        <div className="text-center md:text-left">
          <h3 className="text-xl font-extrabold text-gray-900">
            Banki بنكي
          </h3>
          <p className="text-sm text-gray-600 mt-2 max-w-sm">
            Smart, secure, and simple banking assistance.
            Explore your data with confidence.
          </p>
        </div>

        {/* Center */}
        <div className="flex gap-6">
          <a
            href="#"
            className="text-gray-500 hover:text-green-600 transition"
            aria-label="Facebook"
          >
            <Facebook size={22} />
          </a>
          <a
            href="#"
            className="text-gray-500 hover:text-green-600 transition"
            aria-label="X"
          >
            <X size={22} />
          </a>
          <a
            href="#"
            className="text-gray-500 hover:text-green-600 transition"
            aria-label="Instagram"
          >
            <Instagram size={22} />
          </a>
        </div>

        {/* Right */}
        <div className="text-sm text-gray-500 text-center md:text-right">
          © {new Date().getFullYear()} Banki. <br />
          All rights reserved.
        </div>
      </div>
    </footer>
  );
}
